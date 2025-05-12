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

