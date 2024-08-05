import requests
import json
import xmltodict
from datetime import datetime, timedelta
import pytz
import pandas as pd
import mysql.connector
import pymysql
import warnings
import logging
from io import StringIO
from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from airflow.providers.mysql.hooks.mysql import MySqlHook
from airflow.hooks.S3_hook import S3Hook

warnings.filterwarnings('ignore')
pymysql.install_as_MySQLdb()

# Connection to Mysql
mysql_hook = MySqlHook(mysql_conn_id="mysql_db")
conn = mysql_hook.get_conn()
cur = conn.cursor()

# S3 버킷 연결
bucket = 'shortforecast'
s3_hook = S3Hook(bucket)

# 기상청 단기 예보 데이터를 가져오는 함수
def short_fcst(datetime_date):
    yesterday = datetime_date - timedelta(days=1)
    base_date = yesterday.strftime("%Y%m%d")

    code_df = pd.read_csv("/opt/airflow/data/예보구역_세부구역코드.csv")
    all_results = []  # 모든 결과를 저장할 리스트 초기화
    for i in code_df['구역']:
        url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst'
        params = {
            'serviceKey' : 'jksHK8Ehr+OL3YRjH7551tj8ptFPi74ZpJZteYN+h8xTNHjozOoXdXOIYXAcQHNgurb6vjlylEibJ9LC4RI3sw==',
            'pageNo' : '1',
            'numOfRows' : '1000',
            'dataType' : 'XML',
            'base_date' : base_date,
            'base_time' : '2340',
            'nx' : code_df[code_df['구역'] == i]['nx'].values[0],  # values[0]으로 값 추출
            'ny' : code_df[code_df['구역'] == i]['ny'].values[0]   # values[0]으로 값 추출
        }
        try:
            response = requests.get(url, params=params)
            response.raise_for_status()  # HTTP 요청 실패 시 예외 발생
        except requests.exceptions.RequestException as e:
            print(f"Request failed: {e}")
            continue
        try:
            json_content = json.loads(json.dumps(xmltodict.parse(response.content)))
        except (json.JSONDecodeError, xmltodict.expat.ExpatError) as e:
            print(f"Failed to parse response content: {e}")
            continue
        # 데이터 유효성 확인
        if not json_content or 'response' not in json_content or 'body' not in json_content['response'] or 'items' not in json_content['response']['body'] or 'item' not in json_content['response']['body']['items']:
            print("Invalid response structure.")
            continue
        try:
            df = pd.DataFrame(json_content['response']['body']['items']['item'])
        except KeyError as e:
            print(f"Unexpected response format: {e}")
            continue
        try:
            result_df = pd.DataFrame({
                "예보 날짜" : df[df['category'] == 'POP']['fcstDate'].reset_index(drop=True),
                "예보 시간" : df[df['category'] == 'POP']['fcstTime'].reset_index(drop=True),
                "강수 확률" : df[df['category'] == 'POP']['fcstValue'].reset_index(drop=True),
                "예보구역코드" : code_df[code_df['구역'] == i]['세부코드'].reset_index(drop=True),
                "예보구역이름" : code_df[code_df['구역'] == i]['구역'].reset_index(drop=True),
                "일 최고 기온" : df[df['category'] == 'TMX']['fcstValue'].reset_index(drop=True),
                "일 최저 기온" : df[df['category'] == 'TMN']['fcstValue'].reset_index(drop=True),
                "하늘 상태" : df[df['category'] == 'SKY']['fcstValue'].reset_index(drop=True),
                "강수 형태" : df[df['category'] == 'PTY']['fcstValue'].reset_index(drop=True),
                "1시간 기온" : df[df['category'] == 'TMP']['fcstValue'].reset_index(drop=True),
            })
            # 예보 데이터 재구성
            result_df.loc[0:24, '일 최고 기온'] = df[df['category'] == 'TMX']['fcstValue'].reset_index(drop=True)[0]
            result_df.loc[0:24, '일 최저 기온'] = df[df['category'] == 'TMN']['fcstValue'].reset_index(drop=True)[0]
            result_df.loc[24:48, '일 최고 기온'] = df[df['category'] == 'TMX']['fcstValue'].reset_index(drop=True)[1]
            result_df.loc[24:48, '일 최저 기온'] = df[df['category'] == 'TMN']['fcstValue'].reset_index(drop=True)[1]
            result_df.loc[48:73, '일 최고 기온'] = df[df['category'] == 'TMX']['fcstValue'].reset_index(drop=True)[2]
            result_df.loc[48:73, '일 최저 기온'] = df[df['category'] == 'TMN']['fcstValue'].reset_index(drop=True)[2]
            result_df.loc[0:73, '예보구역코드'] = code_df[code_df['구역'] == i]['세부코드'].values[0]
            result_df.loc[0:73, '예보구역이름'] = code_df[code_df['구역'] == i]['구역'].values[0]
            result_df.reset_index(drop=True, inplace=True)
            all_results.append(result_df)  # 결과를 리스트에 추가
        except Exception as e:
            print(f"Error processing data: {e}")
            continue
    if all_results:
        result_df = pd.concat(all_results, ignore_index=True)
        return result_df  # 모든 결과를 하나의 데이터프레임으로 병합
    else:
        return pd.DataFrame()  # 결과가 없으면 빈 데이터프레임 반환

# 서울 기준 시각
seoul_timezone = pytz.timezone("Asia/Seoul")

# Airflow DAG 설정
default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime.now(seoul_timezone).replace(hour=8, minute=30, second=0, microsecond=0),  # 한국 시간으로 start_date 설정 (오후 4시 30분)
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

dag = DAG(
    'short_weather',
    default_args=default_args,
    description='Fetch middle range weather forecast data and store in MySQL',
    schedule_interval='30 7 * * *',  # 매일 오전 7시 30분 UTC로 실행 (한국 시간으로는 오후 4시 30분)
)

def fetch_short_forecast(datetime_date, **kwargs):
    ti = kwargs['ti']
    result = short_fcst(datetime_date)

    file_name = 'short_forecast.csv'
    result.to_csv(file_name, index=False, encoding='utf-8')
    print(f"Saved short forecast data to {file_name}")

    s3_key = 'short_forecast.csv'
    s3_hook.load_file(filename=file_name, key=s3_key, bucket_name=bucket, replace=True)

    #ti.xcom_push(key='s3_key', value=s3_key)
    ti.xcom_push(key='datetime', value=datetime_date)

    return result

def load_csv_from_s3(s3_hook, bucket_name):
    key = 'short_forecast.csv'
    csv_data = s3_hook.read_key(key, bucket_name)

    # 문자열 데이터를 StringIO 객체로 변환
    csv_stringio = StringIO(csv_data)
    
    # Pandas를 사용하여 CSV 데이터를 DataFrame으로 변환
    df = pd.read_csv(csv_stringio)
    
    return df

def delete_previous_data(**kwargs):
    # SQL 쿼리를 안전하게 실행
    sql = """
        DELETE FROM short_forecast
    """
    cur.execute(sql)
    conn.commit()

    cur.close()
    conn.close()
    print("데이터가 성공적으로 삭제되었습니다.")

def insert_to_mysql(**kwargs):
    ti = kwargs['ti']
    datetime_date = ti.xcom_pull(task_ids='fetch_short_forecast', key='datetime')

    result = load_csv_from_s3(s3_hook, bucket)
    
    try:
        cur.execute("BEGIN;") # 트랜잭션 시작

        for index, row in result.iterrows():
            sql = """
            INSERT INTO short_forecast (fcst_date, fcst_time, pty, reg_nm, reg_temp_id, rm_st, sky, ta_max, ta_min, tmp)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            cur.execute(sql, (
                row['예보 날짜'],
                row['예보 시간'],
                row['강수 형태'],
                row['예보구역이름'],
                row['예보구역코드'],
                row['강수 확률'],
                row['하늘 상태'],
                row['일 최고 기온'],
                row['일 최저 기온'],
                row['1시간 기온']
            ))
        conn.commit()
        cur.close()
        conn.close()
        print("데이터가 성공적으로 삽입되었습니다.")
    except Exception as e:
        conn.rollback()  # 에러 발생 시 롤백
        logging.error("An error occurred during database insertion: %s", e)
        raise  # 에러를 다시 발생시켜 Airflow가 잡을 수 있도록 함

    return result

fetch_data_task = PythonOperator(
    task_id='fetch_short_forecast',
    python_callable=fetch_short_forecast,
    op_args=[datetime.now(seoul_timezone)],  # 오늘 날짜를 가져옵니다.
    dag=dag,
)

delete_previous_data_task = PythonOperator(
    task_id='delete_previous_data',
    python_callable=delete_previous_data,
    dag=dag,
)

insert_to_mysql_task = PythonOperator(
    task_id='insert_to_mysql',
    python_callable=insert_to_mysql,
    dag=dag,
)

fetch_data_task >> delete_previous_data_task >> insert_to_mysql_task
