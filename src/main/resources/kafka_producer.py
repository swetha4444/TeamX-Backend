import json
import uuid
import random
import time
from kafka import KafkaProducer
from pymongo import MongoClient

KAFKA_BROKER = 'localhost:9092'
CONTESTS_FILE = 'src/main/resources/live_cricket_score_with_contest.json'
TEAM_IMG_FILE = 'src/main/resources/team_images.json'
POINTS_TOPIC = 'points'

# MongoDB setup to fetch contest ids
MONGO_URI = "mongodb+srv://ssaseendran:teamx1234@teamxcluster.ybhmxsu.mongodb.net/Login?retryWrites=true&w=majority"
mongo_client = MongoClient(MONGO_URI)
db = mongo_client['Login']
contests_collection = db['contests']

# Example points lists for all formats
t20Points = [
    [{'Type':'Run','Points': 1},{'Type':'Four Bonus','Points': 2},{'Type':'Six Bonus','Points': 2},{'Type':'Half Century Bonus','Points': 8},{'Type':'Century Bonus','Points': 16}],
    [{'Type':'Wicket(Excluding Run Out)','Points': 25},{'Type':'Maiden Over Bonus','Points': 12},{'Type':'3 Wicket Bonus','Points': 4},{'Type':'5 Wicket Bonus','Points': 16}, {'Type':'Maiden Over','Points': 12}],
    [{'Type':'Catch','Points': 8},{'Type':'3 Catch','Points': 4},{'Type':'Stumping','Points': 12},{'Type':'Run Out(Direct - Hit)','Points': 12}, {'Type':'Run Out(Not a direct - Hit)','Points': 6}],
    [{'Type':'Captain','Points': 20}, {'Type':'Vice Captain','Points': 10}]
]
t10Points = [
    [{'Type':'Run','Points': 1},{'Type':'Four Bonus','Points': 1},{'Type':'Six Bonus','Points': 2},{'Type':'30 Runs Bonus','Points': 8},{'Type':'Half Century Bonus','Points': 16}],
    [{'Type':'Wicket(Excluding Run Out)','Points': 25},{'Type':'2 Wicket Bonus','Points': 8},{'Type':'3 Wicket Bonus','Points': 16}, {'Type':'Maiden Over','Points': 12}],
    [{'Type':'Catch','Points': 8},{'Type':'3 Catch','Points': 4},{'Type':'Stumping','Points': 12},{'Type':'Run Out(Direct - Hit)','Points': 12}, {'Type':'Run Out(Not a direct - Hit)','Points': 6}],
    [{'Type':'Captain','Points': 20}, {'Type':'Vice Captain','Points': 10}]
]
odiPoints = [
    [{'Type':'Run','Points': 1},{'Type':'Four Bonus','Points': 1},{'Type':'Six Bonus','Points': 2},{'Type':'Half Century Bonus','Points': 4},{'Type':'Century Bonus','Points': 8}],
    [{'Type':'Wicket(Excluding Run Out)','Points': 25},{'Type':'4 Wicket Bonus','Points': 4},{'Type':'5 Wicket Bonus','Points': 8}, {'Type':'Maiden Over','Points': 12}],
    [{'Type':'Catch','Points': 8},{'Type':'3 Catch','Points': 4},{'Type':'Stumping','Points': 12},{'Type':'Run Out(Direct - Hit)','Points': 12}, {'Type':'Run Out(Not a direct - Hit)','Points': 6}],
    [{'Type':'Captain','Points': 20}, {'Type':'Vice Captain','Points': 10}]
]
testPoints = [
    [{'Type':'Run','Points': 1},{'Type':'Four Bonus','Points': 1},{'Type':'Six Bonus','Points': 2},{'Type':'Half Century Bonus','Points': 4},{'Type':'Century Bonus','Points': 8}],
    [{'Type':'Wicket(Excluding Run Out)','Points': 16},{'Type':'4 Wicket Bonus','Points': 4},{'Type':'5 Wicket Bonus','Points': 8}],
    [{'Type':'Catch','Points': 8},{'Type':'Stumping','Points': 12},{'Type':'Run Out(Direct - Hit)','Points': 12}, {'Type':'Run Out(Not a direct - Hit)','Points': 6}],
    [{'Type':'Captain','Points': 20}, {'Type':'Vice Captain','Points': 10}]
]

def get_random_point():
    points_list = random.choice([t20Points, t10Points, odiPoints, testPoints])
    category = random.choice(points_list)
    point = random.choice(category)
    return {
        "type": point['Type'],
        "point": point['Points']
    }

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BROKER,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

player_names = [
    "Rameesha Shahid", "Christina Gough", "Ameya Kanukuntla", "Shravya Kolcharam",
    "Milena Beresford", "Anne Bierwisch", "Sharanya Sadarangani", "Janet Ronalds",
    "Karthika Vijayaraghavan", "Ashwini Balaji", "Nicole Kingsley", "Iris Edwards",
    "Wilhelmina Garcia", "Asmita Kohli", "Aggeliki Savvani", "Chrysoula Kanta",
    "Nikoleta Dolianiti", "Maria afroditi Vervitsioti", "Maria Polymeri",
    "Elpida Kallous", "Thalia Koula", "Maria Syrioti", "Ioanna Argyropolou",
    "Kapsokavadi Tereza", "Adamantia Makri", "Alekcandra Kourkoulou", "Evangellia Grammenou"
]
roles = ["Batsman", "Bowler", "Allrounder", "WK"]
batting_styles = ["Right Handed Bat", "Left Handed Bat"]
bowling_styles = [
    "Right-arm medium", "Left-arm fast-medium", "Right-arm offbreak",
    "Right-arm legbreak", "Left-arm orthodox", "Right-arm fast-medium", None
]

with open(TEAM_IMG_FILE) as f:
    team_img_map = json.load(f)

def generate_player(match_id, country):
    img = team_img_map.get(country, "")
    return {
        "id": str(uuid.uuid4()),
        "name": random.choice(player_names),
        "role": random.choice(roles),
        "battingStyle": random.choice(batting_styles),
        "bowlingStyle": random.choice(bowling_styles),
        "country": country,
        "playerImg": img,
        "credit": random.randint(5, 10),
        "match_id": match_id  # match_id for MongoDB
    }

with open(CONTESTS_FILE) as f:
    contests = json.load(f)

def send_random_point():
    contest_ids = [str(doc['_id']) for doc in contests_collection.find({}, {'_id': 1})]
    if not contest_ids:
        print("No contests in DB to assign points.")
        return
    contest_id = random.choice(contest_ids)
    point_record = {
        "contest_id": contest_id,
        "points": [get_random_point()]
    }
    producer.send(POINTS_TOPIC, point_record)
    print(f"Sent point record for contest {contest_id}: {point_record}")

while True:
    contest = next((c for c in contests if "data" in c and c["data"]), None)
    if contest:
        # Generate a new ObjectId-like string for contest _id
        contest_id = uuid.uuid4().hex[:24]
        contest['id'] = contest_id
        contest['_id'] = contest_id
        contest['_class'] = "com.teamx.demo.model.Contest"
        producer.send('contests', contest)
        print(f"Sent contest: {contest_id}")

        team_infos = contest["data"][0].get("teamInfo", [])
        for team in team_infos:
            country = team.get("name")
            for _ in range(11):
                player = generate_player(contest_id, country)
                producer.send('players', player)
                print(f"Sent player: {player['name']} for contest {contest_id} (team: {country})")

        send_random_point()
        producer.flush()
        print("All records sent to Kafka. Waiting 2 minutes...")
        time.sleep(120)  # 2 minutes
    else:
        print("No contest with data found.")
        break