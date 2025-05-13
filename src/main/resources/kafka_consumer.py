import json
from kafka import KafkaConsumer
from pymongo import MongoClient
from bson import ObjectId

# Kafka setup
KAFKA_BROKER = 'localhost:9092'
CONTEST_TOPIC = 'contests'
PLAYER_TOPIC = 'players'
POINTS_TOPIC = 'points'

consumer = KafkaConsumer(
    CONTEST_TOPIC, PLAYER_TOPIC, POINTS_TOPIC,
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
points_collection = db['points']

print("Kafka consumer started. Listening for messages...")

for message in consumer:
    if message.topic == CONTEST_TOPIC:
        contest = message.value
        # Use custom id as MongoDB _id (ObjectId)
        contest_id = contest.get('id')
        if contest_id:
            contest_doc = dict(contest)
            contest_doc['_id'] = ObjectId(contest_id) if len(contest_id) == 24 else contest_id
            contest_doc['_class'] = "com.teamx.demo.model.Contest"
            contests_collection.replace_one({'_id': contest_doc['_id']}, contest_doc, upsert=True)
            print(f"Inserted/updated contest: {contest_doc['_id']}")
    elif message.topic == PLAYER_TOPIC:
        player = message.value
        # Use custom id as MongoDB _id (ObjectId)
        player_id = player.get('id')
        match_id = player.get('matchId') or player.get('match_id')
        # Use contest_id as match_id, and store as string or ObjectId as needed
        player_doc = dict(player)
        player_doc['_id'] = ObjectId(player_id) if player_id and len(player_id) == 24 else player_id
        player_doc['match_id'] = match_id  # ensure field is match_id
        if 'matchId' in player_doc:
            del player_doc['matchId']
        players_collection.replace_one({'_id': player_doc['_id']}, player_doc, upsert=True)
        print(f"Inserted/updated player: {player_doc['_id']} (match_id: {player_doc['match_id']})")
    elif message.topic == POINTS_TOPIC:
        point_msg = message.value
        contest_id = point_msg['contest_id']
        new_point = point_msg['points'][0]
        # Try to update existing record, else insert new
        result = points_collection.update_one(
            {'contest_id': contest_id},
            {'$push': {'points': new_point}},
            upsert=True
        )
        print(f"Added point {new_point} to contest {contest_id} in points collection.")