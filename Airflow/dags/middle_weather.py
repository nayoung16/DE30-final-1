import json
import xmltodict
import requests
import pandas as pd
from datetime import datetime, timedelta
import bs4
import warnings
import pytz
import logging
from io import StringIO
import pymysql
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
bucket = 'middleforecast'
s3_hook = S3Hook(bucket)


def middle_temp(datetime_date):
    file_path = '/opt/airflow/data/예보구역_세부구역코드.csv'
    regIdName = pd.read_csv(file_path)
    # 중기기온조회 저장되는 테이블
    responses_ta = []
    for reg_id in regIdName['세부코드']:
    
        url = 'http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa'
        params = {
            'serviceKey' : 'jksHK8Ehr+OL3YRjH7551tj8ptFPi74ZpJZteYN+h8xTNHjozOoXdXOIYXAcQHNgurb6vjlylEibJ9LC4RI3sw==',
            'pageNo' : '1',
            'numOfRows' : '10',
            'dataType' : 'XML',
            'regId' : reg_id,
            'tmFc' : datetime_date+"0600" # 매일 06시, 18시 업데이트 필요
        }
        
        response = requests.get(url, params=params)
        if response.status_code == 200:
            responses_ta.append(response.content)
        else:
            print(f"Failed to retrieve data for regId: {reg_id}")
    
    result_ta = pd.DataFrame()
    for num, response in enumerate(responses_ta):
        xml_obj = bs4.BeautifulSoup(response, 'lxml-xml')
        rows = xml_obj.findAll('item')
        for i in range(0, len(rows)):
            # 구역 이름과 온도 데이터를 추출하여 DataFrame에 추가
            tmp_ta = pd.DataFrame({
                rows[i].find('regId').name: [rows[i].find('regId').text],
                rows[i].find('taMin3').name: [rows[i].find('taMin3').text],
                rows[i].find('taMax3').name: [rows[i].find('taMax3').text],
                rows[i].find('taMin4').name: [rows[i].find('taMin4').text],
                rows[i].find('taMax4').name: [rows[i].find('taMax4').text],
                rows[i].find('taMin5').name: [rows[i].find('taMin5').text],
                rows[i].find('taMax5').name: [rows[i].find('taMax5').text],
                rows[i].find('taMin6').name: [rows[i].find('taMin6').text],
                rows[i].find('taMax6').name: [rows[i].find('taMax6').text],
                rows[i].find('taMin7').name: [rows[i].find('taMin7').text],
                rows[i].find('taMax7').name: [rows[i].find('taMax7').text]
            })
            tmp_ta['regWeatherId'] = regIdName['예보구역코드'][num]
            tmp_ta['regNm'] = regIdName['구역'][num]
            result_ta = pd.concat([tmp_ta, result_ta])
            
    # DataFrame의 인덱스 초기화
    result_ta = result_ta.reset_index(drop=True)
    return result_ta

def middle_weather(datetime_date):
    df_middleweather = pd.read_csv("/opt/airflow/data/중기육상예보구역.csv")
    reg_id =df_middleweather["예보구역코드"]
    responses_rn_wf = []
    for i in reg_id:
        url = 'http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst'
        params = {
            'serviceKey' : 'jksHK8Ehr+OL3YRjH7551tj8ptFPi74ZpJZteYN+h8xTNHjozOoXdXOIYXAcQHNgurb6vjlylEibJ9LC4RI3sw==',
            'pageNo' : '1',
            'numOfRows' : '10',
            'dataType' : 'XML',
            'regId' : i,
            'tmFc' : datetime_date+"0600" # 매일 06시, 18시 업데이트 필요
        }
    
        response = requests.get(url, params=params)
        if response.status_code == 200:
            responses_rn_wf.append(response.content)
        else:
            print(f"Failed to retrieve data for regId: {reg_id}")
    
    result_rn_wf = pd.DataFrame()
    for response in responses_rn_wf:
      xml_obj = bs4.BeautifulSoup(response, 'lxml-xml')
      rows = xml_obj.findAll('item')
      for i in range(0, len(rows)):
          tmp_rn_wf = pd.DataFrame({
                  rows[i].find('regId').name:[rows[i].find('regId').text],
                  rows[i].find('rnSt3Am').name:[rows[i].find('rnSt3Am').text],
                  rows[i].find('rnSt3Pm').name:[rows[i].find('rnSt3Pm').text],
                  rows[i].find('rnSt4Am').name:[rows[i].find('rnSt4Am').text],
                  rows[i].find('rnSt4Pm').name:[rows[i].find('rnSt4Pm').text],
                  rows[i].find('rnSt5Am').name:[rows[i].find('rnSt5Am').text],
                  rows[i].find('rnSt5Pm').name:[rows[i].find('rnSt5Pm').text],
                  rows[i].find('rnSt6Am').name:[rows[i].find('rnSt6Am').text],
                  rows[i].find('rnSt6Pm').name:[rows[i].find('rnSt6Pm').text],
                  rows[i].find('rnSt7Am').name:[rows[i].find('rnSt7Am').text],
                  rows[i].find('rnSt7Pm').name:[rows[i].find('rnSt7Pm').text],
                  rows[i].find('wf3Am').name:[rows[i].find('wf3Am').text],
                  rows[i].find('wf3Pm').name:[rows[i].find('wf3Pm').text],
                  rows[i].find('wf4Am').name:[rows[i].find('wf4Am').text],
                  rows[i].find('wf4Pm').name:[rows[i].find('wf4Pm').text],
                  rows[i].find('wf5Am').name:[rows[i].find('wf5Am').text],
                  rows[i].find('wf5Pm').name:[rows[i].find('wf5Pm').text],
                  rows[i].find('wf6Am').name:[rows[i].find('wf6Am').text],
                  rows[i].find('wf6Pm').name:[rows[i].find('wf6Pm').text],
                  rows[i].find('wf7Am').name:[rows[i].find('wf7Am').text],
                  rows[i].find('wf7Pm').name:[rows[i].find('wf7Pm').text]})
          result_rn_wf = pd.concat([tmp_rn_wf,result_rn_wf])
    result_rn_wf = result_rn_wf.rename(columns={"regId":"regWeatherId"})
    return result_rn_wf

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
    'middle_weather',
    default_args=default_args,
    description='Fetch middle range weather forecast data and store in MySQL',
    schedule_interval='30 7 * * *',  # 매일 오전 7시 30분 UTC로 실행 (한국 시간으로는 오후 4시 30분)
)

def fetch_middle_forecast(datetime_date, **kwargs):
    ti = kwargs['ti']

    merged_result = pd.merge(middle_temp(datetime_date), middle_weather(datetime_date))
    print(merged_result)

    # 현재 날짜와 시간을 문자열로 포맷팅
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")


    # CSV 파일로 저장
    # file_name = f'merged_middle_forecast_{datetime_date}_{current_time}.csv'
    file_name = f'merged_middle_forecast.csv'
    merged_result.to_csv(file_name, index=False, encoding='utf-8')
    print(f"Saved merged forecast data to {file_name}")
    
    # S3로 csv 업로드
    s3_key = f'merged_middle_forecast.csv'
    s3_hook.load_file(filename=file_name, key=s3_key, bucket_name=bucket, replace=True)

    # S3 키를 XCom으로 전달
    ti.xcom_push(key='s3_key', value=s3_key)
    ti.xcom_push(key='datetime', value=datetime_date)
    
    return merged_result

def load_csv_from_s3(s3_hook, bucket_name, key):
    csv_data = s3_hook.read_key(key, bucket_name)
    
    # 문자열 데이터를 StringIO 객체로 변환
    csv_stringio = StringIO(csv_data)
    
    # Pandas를 사용하여 CSV 데이터를 DataFrame으로 변환
    df = pd.read_csv(csv_stringio)
    
    return df

def delete_previous_data(datetime_date, **kwargs):
    # 예측일로부터 3일 후부터 7일 후까지의 날짜 계산
    date3 = (datetime.strptime(datetime_date, "%Y%m%d") + timedelta(days=3)).strftime("%Y%m%d")
    date7 = (datetime.strptime(datetime_date, "%Y%m%d") + timedelta(days=7)).strftime("%Y%m%d")
    
    # SQL 쿼리를 안전하게 실행
    sql = """
        DELETE FROM middle_forecast WHERE fcst_date BETWEEN %s AND %s
    """
    cur.execute(sql, (date3, date7))
    conn.commit()

    cur.close()
    conn.close()
    print("데이터가 성공적으로 삭제되었습니다.")


# MySQL에 데이터 삽입하는 task 정의
def insert_to_mysql(**kwargs):
    ti = kwargs['task_instance']
    # execution_date = kwargs['execution_date'].strftime("%Y%m%d")
    datetime_date = ti.xcom_pull(task_ids='fetch_middle_forecast', key='datetime')
    
    # XCom에서 데이터 pull
    s3_key = ti.xcom_pull(task_ids='fetch_middle_forecast', key='s3_key')


    # S3에서 csv 읽기
    merged_result = load_csv_from_s3(s3_hook, bucket, s3_key)
    
    try:
        cur.execute("BEGIN;") # 트랜잭션 시작
        
        for index, row in merged_result.iterrows():
            sql = """
                INSERT INTO middle_forecast (fcst_date, reg_nm, reg_temp_id, reg_weather_id, rn_st_am, rn_st_pm, ta_max, ta_min, wf_am, wf_pm)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """
            data = [
                (
                    (datetime.strptime(datetime_date, "%Y%m%d") + timedelta(days=i)).strftime("%Y%m%d"),
                    row["regNm"], row["regId"],row["regWeatherId"], row[f"rnSt{i}Am"],
                    row[f"rnSt{i}Pm"],row[f"taMax{i}"],row[f"taMin{i}"], row[f"wf{i}Am"], row[f"wf{i}Pm"]
                ) for i in range(3, 8)
            ]
            print(data)
            cur.executemany(sql, data)
    
        conn.commit()
        cur.close()
        conn.close()
        print("데이터가 성공적으로 삽입되었습니다.")
        
    except Exception as e:
        conn.rollback()  # 에러 발생 시 롤백
        logging.error("An error occurred during database insertion: %s", e)
        raise  # 에러를 다시 발생시켜 Airflow가 잡을 수 있도록 함

    return merged_result


# Airflow task 정의
fetch_data_task = PythonOperator(
    task_id='fetch_middle_forecast',
    python_callable=fetch_middle_forecast,
    op_args=[(datetime.now(seoul_timezone) - timedelta(days=1)).strftime("%Y%m%d")],  # 어제 날짜
    dag=dag,
)

delete_previous_data_task = PythonOperator(
    task_id='delete_previous_data',
    python_callable=delete_previous_data,
    op_args=[(datetime.now(seoul_timezone) - timedelta(days=1)).strftime("%Y%m%d")],  # 어제 날짜
    dag=dag,
)

insert_to_mysql_task = PythonOperator(
    task_id='insert_to_mysql',
    python_callable=insert_to_mysql,
    dag=dag,
)

fetch_data_task >> delete_previous_data_task >> insert_to_mysql_task
