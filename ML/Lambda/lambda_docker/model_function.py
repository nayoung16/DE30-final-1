import json
import boto3
import joblib
import pandas as pd
import io
import os
import random
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def load_model_style():
    s3 = boto3.client('s3')
    bucket_name = 'model-pklfile'
    model_key = 'camping_style_rec_model.pkl'
    local_model_path = '/tmp/model.pkl'

    # S3에서 모델 파일 다운로드
    s3.download_file(bucket_name, model_key, local_model_path)

    # 다운로드한 파일에서 데이터 읽기

    # pickle을 사용하여 모델 로드
    model = joblib.load(local_model_path)
    
    print("Loaded model type:", type(model)) 
    return model

def get_camping_style_info():
    s3 = boto3.client('s3')
    bucket_name = 'gocamping'
    model_key = 'camp_style_info.csv'
    obj = s3.get_object(Bucket=bucket_name,Key=model_key)
    df = pd.read_csv(io.BytesIO( obj["Body"].read() ) )
    return df

def get_recommend_style(style_model, user_input):
    user_df = pd.DataFrame([user_input])
    recommended_style = style_model.predict(user_df)
    return recommended_style[0]