import logging
import requests
import pprint
import json
import xmltodict
import pandas as pd
from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.hooks.S3_hook import S3Hook
from airflow.models import Variable
from io import StringIO
from airflow.providers.mysql.hooks.mysql import MySqlHook
from datetime import datetime

bucket = "gocamping"
s3_hook = S3Hook(bucket)

# Connection to Mysql
mysql_hook = MySqlHook(mysql_conn_id="mysql_db")
conn = mysql_hook.get_conn()
cur = conn.cursor()

def fetch_goCamping(**kwargs):
    ti = kwargs['ti']
    
    service_key = "jksHK8Ehr+OL3YRjH7551tj8ptFPi74ZpJZteYN+h8xTNHjozOoXdXOIYXAcQHNgurb6vjlylEibJ9LC4RI3sw=="

    # API 기본 URL 및 엔드포인트 설정
    base_url = "http://apis.data.go.kr/B551011/GoCamping/"
    url = "basedList"
    params = {
        "serviceKey": service_key,  # 여기에 실제 서비스 키를 입력하세요
        "numOfRows": 4000,
        "pageNo": 1,
        "MobileOS": "etc",
        "MobileApp": "etc",
    }

    # GET 요청 보내기
    response = requests.get(base_url + url, params=params)

    data_dict = xmltodict.parse(response.text)
    data = data_dict['response']['body']['items']['item']
    
   # DataFrame 생성
    default_df = pd.DataFrame(data)
    etc_df = pd.DataFrame(data)

    # 열 이름 변경
    default_columns = {
            'contentId': 'content_id','doNm': 'do_nm','facltNm': 'faclt_nm', 'firstImageUrl': 'first_image_url',
            'hvofBgnde': 'hvof_bgnde', 'hvofEnddle': 'hvof_enddle', 'lctCl': 'lct_cl', 'lineIntro': 'line_intro',
            'mapX': 'mapx', 'mapY': 'mapy', 'operDeCl': 'open_de_cl', 'operPdCl': 'open_pdcl',
            'resveUrl': 'resve_url', 'sigunguNm': 'sigungu_nm'
        }
    etc_columns = {
        'contentId': 'content_id','animalCmgCl':'animal_cmg_cl','autoSiteCo':'auto_site_co','caravAcmpnyAt':'carav_acmpny_at',
        'caravInnerFclty':'carav_inner_fcity','caravSiteCo':'carav_site_co','clturEvent':'cltur_event','clturEventAt':'cltur_event_at',
        'eqpmnLendCl':'eqpmn_lend_cl','exprnProgrm':'exprn_progrm','exprnProgrmAt':'exprn_progrm_at','glampInnerFclty':'glamp_inner_fcity',
        'glampSiteCo':'glamp_site_co','gnrlSiteCo':'gnrl_site_co','posblFcltyCl':'posbl_fclty_cl','posblFcltyEtc':'posbl_fclty_etc',
        'sbrsCl':'sbrs_cl','sbrsEtc':'sbrs_etc','themaEnvrnCl':'thema_envrn_cl','trlerAcmpnyAt':'trier_acmpny_at'
    }
        

        # 열 이름 매핑
    default_df.rename(columns=default_columns, inplace=True)
    etc_df.rename(columns=etc_columns, inplace=True)
    
    default_df.to_csv("Gocamping_default_info.csv", index=False, encoding="utf-8")
    etc_df.to_csv("Gocamping_etc_info.csv", index=False, encoding="utf-8")
    

    default_s3_key = "gocamping_default_info.csv"
    etc_s3_key = "gocamping_etc_info.csv"
    s3_hook.load_file(filename="Gocamping_default_info.csv", key=default_s3_key, bucket_name=bucket, replace=True)
    s3_hook.load_file(filename="Gocamping_etc_info.csv", key=etc_s3_key, bucket_name=bucket, replace=True)
    
    ti.xcom_push(key="default_s3_key", value=default_s3_key)
    ti.xcom_push(key="etc_s3_key", value=etc_s3_key)
    
    return default_df, etc_df

def load_csv_from_s3(s3_hook, bucket_name, key):
    csv_data = s3_hook.read_key(key, bucket_name)
    
    csv_stringio = StringIO(csv_data)
    
    df = pd.read_csv(csv_stringio)
    return df

def insert_to_mysql(**kwargs):
    ti = kwargs['ti']
    
    default_s3_key = ti.xcom_pull(task_ids='fetch_goCamping', key='default_s3_key')
    etc_s3_key = ti.xcom_pull(task_ids='fetch_goCamping', key='etc_s3_key')
    
    try:
    
        default_df = load_csv_from_s3(s3_hook, bucket, default_s3_key)
        etc_df = load_csv_from_s3(s3_hook, bucket, etc_s3_key)

        # DataFrame 내의 nan 값을 None으로 변경
        default_df = default_df.where(pd.notna(default_df), None)
        etc_df = etc_df.where(pd.notna(etc_df), None)

        def check_and_insert(df, table_name, columns):
            for index, row in df.iterrows():
                placeholders = ', '.join(['%s'] * len(columns))
                columns_str = ', '.join(columns)

                # Create ON DUPLICATE KEY UPDATE clause
                update_str = ', '.join([f"{col}=VALUES({col})" for col in columns])

                # Insert or update if exists
                insert_sql = f"""
                    INSERT INTO {table_name} ({columns_str})
                    VALUES ({placeholders})
                    ON DUPLICATE KEY UPDATE {update_str}
                """

                data = [row[col] for col in columns]
                cur.execute(insert_sql, data)
                conn.commit()

        # Insert default data
        default_columns = ["content_id", "addr1", "addr2", "direction", "do_nm", "faclt_nm", "first_image_url", "homepage", 
                 "hvof_bgnde", "hvof_enddle", "induty", "intro", "lct_cl", "line_intro", "mapx", "mapy", 
                 "open_de_cl", "open_pdcl", "resve_url", "sigungu_nm", "tel"]
        check_and_insert(default_df, 'camp_default_info', default_columns)
        logging.info("default 데이터가 성공적으로 삽입되었습니다")

        # Insert etc data
        etc_columns = ["content_id", "animal_cmg_cl", "auto_site_co", "carav_acmpny_at", "carav_inner_fcity", "carav_site_co",
                       "cltur_event", "cltur_event_at", "eqpmn_lend_cl",  "exprn_progrm", "exprn_progrm_at",
                       "glamp_inner_fcity", "glamp_site_co", "gnrl_site_co", "posbl_fclty_cl", "posbl_fclty_etc",
                       "sbrs_cl", "sbrs_etc", "thema_envrn_cl", "tooltip", "trier_acmpny_at"]
        check_and_insert(etc_df, 'camp_etc_info', etc_columns)
        logging.info("etc 데이터가 성공적으로 삽입되었습니다")

        cur.close()
        conn.close()
        
    except Exception as e:
        if conn:
            conn.rollback()
        logging.error("An error occurred: %s", e)
        raise
    
    return default_df, etc_df

dag = DAG(
    'gocamping_data_pipeline',
    default_args={
        'owner': 'airflow',
        'start_date': datetime(2024, 7, 25),
        'retries': 1,
    },
    schedule_interval='0 0 * * 1',  # 매주 월요일 00:00에 실행
)

fetch_data_task = PythonOperator(
    task_id='fetch_goCamping',
    python_callable=fetch_goCamping,
    dag=dag
)

insert_to_mysql_task = PythonOperator(
    task_id='insert_to_mysql',
    python_callable=insert_to_mysql,
    dag=dag
)

fetch_data_task >> insert_to_mysql_task
