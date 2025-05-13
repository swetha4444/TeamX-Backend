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

        producer.flush()
        print("All records sent to Kafka. Waiting 2 minutes...")
        time.sleep(120)  # 2 minutes
    else:
        print("No contest with data found.")
        break