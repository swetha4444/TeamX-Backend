import random
from pymongo import MongoClient

# MongoDB setup
MONGO_URI = "mongodb+srv://ssaseendran:teamx1234@teamxcluster.ybhmxsu.mongodb.net/Login?retryWrites=true&w=majority"
mongo_client = MongoClient(MONGO_URI)
db = mongo_client['Login']
contests_collection = db['contests']
points_collection = db['points']

# Example points lists for all formats (as in your producer)
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

def init_points_for_all_contests():
    contest_ids = [str(doc['_id']) for doc in contests_collection.find({}, {'_id': 1})]
    for contest_id in contest_ids:
        points = []
        used_types = set()
        while len(points) < 10:
            p = get_random_point()
            # Ensure unique type per contest's initial points
            if p['type'] not in used_types:
                points.append(p)
                used_types.add(p['type'])
        points_collection.replace_one(
            {'contest_id': contest_id},
            {'contest_id': contest_id, 'points': points},
            upsert=True
        )
        print(f"Initialized 10 points for contest {contest_id}")

if __name__ == "__main__":
    init_points_for_all_contests()