from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import OneHotEncoder
import pandas as pd
import random
import os
import boto3
from io import StringIO
from sqlalchemy import create_engine
from sqlalchemy import text
from scipy.sparse import csr_matrix

def connect_s3():
  s3_client = boto3.client('s3')
  bucket_name = 'gocamping'
  file_key = 'camp_style_info.csv'
  response = s3_client.get_object(Bucket=bucket_name, Key=file_key)
  data2 = pd.read_csv(StringIO(response['Body'].read().decode('utf-8')))

  return data2

def connect_rds():
  # 데이터베이스 연결 정보 설정
  db_username = os.getenv('DB_USERNAME')
  db_password = os.getenv('DB_PASSWORD')
  db_host = os.getenv('DB_HOST')
  db_port = os.getenv('DB_PORT')
  db_name = os.getenv('DB_NAME')

  # MySQL 데이터베이스 연결 문자열 생성
  connection_string = f'mysql+pymysql://{db_username}:{db_password}@{db_host}:{db_port}/{db_name}'

  # SQLAlchemy 엔진 생성
  engine = create_engine(connection_string)

  # SQL 쿼리를 통해 데이터 가져오기
  query1 = 'SELECT content_id, intro, lct_cl, do_nm, faclt_nm FROM camp_default_info;'
  camp_default_data = pd.read_sql_query(text(query1), engine.connect())

  query2 = 'SELECT content_id, thema_envrn_cl, posbl_fclty_cl FROM camp_etc_info;'
  camp_etc_data = pd.read_sql_query(text(query2), engine.connect())

  # content_id를 기준으로 두 데이터프레임 조인
  data1 = pd.merge(camp_default_data, camp_etc_data, on='content_id')

  return data1

# TF-IDF 벡터화기 정의 및 벡터화
def fit_tfidf_vectorizers(data1, data2):
    vectorizer = TfidfVectorizer()
    envrn_thema_text = data1['thema_envrn_cl'] + ' ' + data1['posbl_fclty_cl']
    tfidf_matrix_data1 = vectorizer.fit_transform(envrn_thema_text)
    tfidf_matrix_data2 = vectorizer.transform(data2['설명'])
    return vectorizer, tfidf_matrix_data1, tfidf_matrix_data2

# 차원 맞추기 함수
def pad_matrix(matrix, target_shape):
    if matrix.shape[1] < target_shape:
        # Create a new matrix with the target shape and fill it with zeros
        padded_matrix = csr_matrix((matrix.shape[0], target_shape))
        padded_matrix[:, :matrix.shape[1]] = matrix
        return padded_matrix
    else:
        return matrix
    
def predict_camping_spot(style_name, doNm=None, envrn=None, thema=None, top_n=10):
  data1 = connect_rds()
  data2 = connect_s3()

  data1 = data1.fillna('')
  data2 = data2.fillna('')

  data1 = pd.DataFrame(data1)
  # TF-IDF 벡터화
  tfidf_vector = TfidfVectorizer()
  tfidf_matrix_data1 = tfidf_vector.fit_transform(data1['intro'])
  tfidf_matrix_data2 = tfidf_vector.transform(data2['설명'])

  # 환경 및 테마 특성 벡터화
  envrn_vectorizer = TfidfVectorizer()
  thema_vectorizer = TfidfVectorizer()

  envrn_tfidf_matrix = envrn_vectorizer.fit_transform(data1['lct_cl'])
  thema_tfidf_matrix = thema_vectorizer.fit_transform(data1['thema_envrn_cl'])

  camping_categories = {
    '일반야영지': ['백패킹', '미니멀캠핑', '캠프닉', '모토캠핑', '브롬핑'],
    '자동차야영지': ['오토캠핑', '차박캠핑', '풀캠핑', '모토캠핑', '브롬핑'],
    '카라반': ['오토캠핑', '차박캠핑'],
    '글램핑': ['글램핑']
  }

  # 데이터프레임 생성
  df_IctCl = pd.DataFrame(data1)
  df_themaEnvrnCl = pd.DataFrame(data1)
  df_posblFcltyCl = pd.DataFrame(data1)
  df = pd.DataFrame(data1)
  df2 = pd.DataFrame(data2)

  # 대주제별 리스트 생성
  산 = df_IctCl[df_IctCl['lct_cl'].str.contains('산', na=False)]['lct_cl'].tolist()
  숲 = df_IctCl[df_IctCl['lct_cl'].str.contains('숲', na=False)]['lct_cl'].tolist()
  해변 = df_IctCl[df_IctCl['lct_cl'].str.contains('해변', na=False)]['lct_cl'].tolist()
  도심 = df_IctCl[df_IctCl['lct_cl'].str.contains('도심', na=False)]['lct_cl'].tolist()

  # 테마 및 시설 대주제별 리스트 생성
  놀거리 = (
      df_themaEnvrnCl[df_themaEnvrnCl['thema_envrn_cl'].str.contains('여름물놀이|낚시', na=False)]['thema_envrn_cl'].tolist(),
      df_posblFcltyCl[df_posblFcltyCl['posbl_fclty_cl'].str.contains('낚시|어린이놀이시설|청소년체험시설|농어촌체험시설|', na=False)]['posbl_fclty_cl'].tolist()
  )
  산책길 = (
      df_themaEnvrnCl[df_themaEnvrnCl['thema_envrn_cl'].str.contains('걷기길|일출명소|일몰명소', na=False)]['thema_envrn_cl'].tolist(),
      df_posblFcltyCl[df_posblFcltyCl['posbl_fclty_cl'].str.contains('산책로', na=False)]['posbl_fclty_cl'].tolist()
  )
  꽃놀이 = data1[data1['thema_envrn_cl'].str.contains('봄꽃여행|가을단풍명소|겨울눈꽃명소', na=False)]['thema_envrn_cl'].tolist()
  액티비티 = (
      df_themaEnvrnCl[df_themaEnvrnCl['thema_envrn_cl'].str.contains('액티비티|수상레저|항공레저|스키|항공레저', na=False)]['thema_envrn_cl'].tolist(),
      df_posblFcltyCl[df_posblFcltyCl['posbl_fclty_cl'].str.contains('계곡 물놀이|수영장|강/물놀이|해수욕|수상레저', na=False)]['posbl_fclty_cl'].tolist()
  )

  # 범주형 데이터 처리
  encoder = OneHotEncoder(sparse=True, handle_unknown='ignore')
  encoded_features = encoder.fit_transform(df[['lct_cl']])

  text_vectorizer, tfidf_matrix_data1, tfidf_matrix_data2 = fit_tfidf_vectorizers(df, df2)

  # 타겟 차원 설정
  target_shape = max(tfidf_matrix_data1.shape[1], tfidf_matrix_data2.shape[1])
  padded_tfidf_matrix_data1 = pad_matrix(tfidf_matrix_data1, target_shape)
  padded_tfidf_matrix_data2 = pad_matrix(tfidf_matrix_data2, target_shape)

  if style_name not in df2['이름'].values:
    return "추천 스타일을 찾을 수 없습니다."

  # 선택한 스타일의 설명 벡터 추출
  style_index = df2[df2['이름'] == style_name].index[0]
  style_vector = padded_tfidf_matrix_data2[style_index]

  # 선택한 지역(doNm) 내 캠핑장 필터링
  if doNm == None:
    filtered_data1 = df
  else:
    filtered_data1 = df[df['do_nm'] == doNm]
    if filtered_data1.empty:
      return f"{doNm} 지역에서 추천할 수 있는 캠핑장이 없습니다."

  # 환경 및 테마 특성 벡터 생성
  if envrn:
    envrn_vector = text_vectorizer.transform([envrn])
    envrn_vector = pad_matrix(envrn_vector, target_shape)
  else:
    envrn_vector = csr_matrix((1, target_shape))
  if thema:
    thema_vector = text_vectorizer.transform([thema])
    thema_vector = pad_matrix(thema_vector, target_shape)
  else:
    thema_vector = csr_matrix((1, target_shape))

  # 필터링된 캠핑장에 대한 TF-IDF 행렬 추출
  filtered_indices = filtered_data1.index
  filtered_tfidf_matrix = padded_tfidf_matrix_data1[filtered_indices]

  # 유사도 계산
  description_similarities = cosine_similarity(style_vector, filtered_tfidf_matrix).flatten()
  envrn_similarities = cosine_similarity(envrn_vector, filtered_tfidf_matrix).flatten()
  thema_similarities = cosine_similarity(thema_vector, filtered_tfidf_matrix).flatten()

  # 유사도 결합
  combined_similarities = (description_similarities + envrn_similarities + thema_similarities) / 3

  # 상위 top_n개의 유사도를 가진 캠핑장 선택
  top_indices = combined_similarities.argsort()[-top_n:][::-1]
  top_camps = filtered_data1.iloc[top_indices]['faclt_nm'].values

  # 상위 top_n개 중에서 5개를 랜덤하게 선택
  if len(top_camps) < 5:
    return "충분한 캠핑장이 없습니다."

  recommended_camps = random.sample(list(top_camps), 5)

  return recommended_camps