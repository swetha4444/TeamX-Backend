import json
import uuid
import random
import time
from kafka import KafkaProducer

KAFKA_BROKER = 'localhost:9092'
CONTESTS_FILE = 'src/main/resources/live_cricket_score_with_contest.json'
TEAM_IMG_FILE = 'src/main/resources/team_images.json'

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BROKER,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

