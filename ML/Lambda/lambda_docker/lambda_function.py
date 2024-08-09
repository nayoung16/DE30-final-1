import json
import os
import random
from model_function import load_model_style
from model_function import get_recommend_style
from model_function import get_camping_style_info
from rec_camping_function import predict_camping_spot

style_model = load_model_style()

camp_style_info = get_camping_style_info()

def handler(event, context):
    try:
        data = json.loads(event['body'])
    
        required_int_fields = ['gear_amount', 'convenience_facility', 'activity', 'companion', 'nature', 'transport', 'comfort']
        required_str_fields = ['envrn_filter', 'thema_filter']
        doNm_fields = ['doNm']

        all_required_fields = required_int_fields + required_str_fields

        missing_fields = [field for field in all_required_fields if field not in data]
        missing_field_doNm = [field for field in doNm_fields if field not in data]
        if missing_field_doNm:
            return {
                "statusCode": 400,
                "body": json.dumps({"error": f"Missing fields: {', '.join(missing_field_doNm)}"})
            }
        if missing_fields:
            return {
                "statusCode": 400,
                "body": json.dumps({"error": f"Missing fields: {', '.join(missing_fields)}"})
            }
    
        for field in required_int_fields:
            if not isinstance(data[field], int):
                return {
                    "statusCode": 400,
                    "body": json.dumps({"error": f"Expected integer for field {field}, got {type(data[field]).__name__}"})
                }
            
        for field in required_str_fields:
            if not isinstance(data[field], str):
                return {
                    "statusCode": 400,
                    "body": json.dumps({"error": f"Expected str for field {field}, got {type(data[field]).__name__}"})
                }
    
        rec_fields = ['gear_amount', 'convenience_facility', 'activity', 'companion', 'nature', 'transport', 'comfort']
        recommended_style = get_recommend_style(style_model, {k: data[k] for k in rec_fields})
        recommended_camps = predict_camping_spot(recommended_style, data['doNm'], data['envrn_filter'], data['thema_filter'])

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({"recommended_style": recommended_style,"recommended_camps":recommended_camps})
        }

    except Exception as e:
        return {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }