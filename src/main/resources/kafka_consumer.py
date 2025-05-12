import json
from kafka import KafkaConsumer
from pymongo import MongoClient
from bson import ObjectId

# Kafka setup
KAFKA_BROKER = 'localhost:9092'
CONTEST_TOPIC = 'contests'
PLAYER_TOPIC = 'players'

consumer = KafkaConsumer(
    CONTEST_TOPIC, PLAYER_TOPIC,
    bootstrap_servers=KAFKA_BROKER,
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
    auto_offset_reset='earliest',
    group_id='demo-group'
)

# MongoDB setup
MONGO_URI = "mongodb+srv://ssaseendran:teamx1234@teamxcluster.ybhmxsu.mongodb.net/Login?retryWrites=true&w=majority"
mongo_client = MongoClient(MONGO_URI)
db = mongo_client['Login']
contests_collection = db['contests']
players_collection = db['players']

