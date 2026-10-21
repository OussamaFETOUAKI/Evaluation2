import subprocess
import urllib.request
import json
import os

repo_dir = r"C:\Users\dell\Documents\microservices\Evaluation2"
repo_name = "Evaluation2"
repo_desc = "Évaluation 2 : Architecture multi-couches Hibernate, JPA & Spring IoC (Gestion de stock, Gestion de projets, État civil)"

# 1. Obtenir le token depuis le credential helper git
p = subprocess.Popen(
    ["git", "credential", "fill"],
    stdin=subprocess.PIPE,
    stdout=subprocess.PIPE,
    stderr=subprocess.PIPE,
    text=True
)
out, err = p.communicate("protocol=https\nhost=github.com\n\n")
token = None
username = None
for line in out.splitlines():
    if line.startswith("username="):
        username = line.split("=", 1)[1]
    if line.startswith("password="):
        token = line.split("=", 1)[1]

if not token:
    print("Erreur: Impossible de récupérer le token GitHub via git credential.")
    exit(1)

print(f"Utilisateur GitHub détecté : {username}")

# 2. Création du repository GitHub via l'API REST
create_data = json.dumps({
    "name": repo_name,
    "description": repo_desc,
    "private": False
}).encode("utf-8")

req = urllib.request.Request(
    "https://api.github.com/user/repos",
    data=create_data,
    headers={
        "Authorization": f"Bearer {token}",
        "User-Agent": "Antigravity",
        "Accept": "application/vnd.github.v3+json",
        "Content-Type": "application/json"
    }
)

try:
    with urllib.request.urlopen(req) as resp:
        res = json.loads(resp.read().decode("utf-8"))
        print("Dépôt GitHub créé avec succès :", res.get("html_url"))
except urllib.error.HTTPError as e:
    body = e.read().decode("utf-8")
    if e.code == 422 and "already exists" in body:
        print("Le dépôt GitHub 'Evaluation2' existe déjà. Poursuite vers le push...")
    else:
        print(f"HTTPError lors de la création du repo : {e.code} {body}")
except Exception as e:
    print("Exception lors de la création du repo :", e)

# 3. Commandes Git dans le répertoire du projet
def run_git(args):
    print("Exécution : git", " ".join(args))
    res = subprocess.run(["git"] + args, cwd=repo_dir, capture_output=True, text=True)
    if res.stdout.strip():
        print("STDOUT:", res.stdout.strip())
    if res.stderr.strip():
        print("STDERR:", res.stderr.strip())
    return res.returncode

if not os.path.exists(os.path.join(repo_dir, ".git")):
    run_git(["init"])

run_git(["config", "user.name", "OussamaFETOUAKI"])
run_git(["config", "user.email", "oussamafetouaki2@gmail.com"])
run_git(["add", "."])
run_git(["commit", "-m", "Initial commit: Évaluation 2 — Hibernate, JPA & Spring IoC (3 exercices, captures d'écran terminal & documentation README)"])
run_git(["branch", "-M", "main"])

remote_with_token = f"https://{username}:{token}@github.com/{username}/{repo_name}.git"
clean_remote = f"https://github.com/{username}/{repo_name}.git"

run_git(["remote", "remove", "origin"])
run_git(["remote", "add", "origin", remote_with_token])

print("Push vers GitHub...")
push_code = run_git(["push", "-u", "origin", "main", "--force"])

# Nettoyage de l'URL distante pour ne pas laisser de token dans la config locale
run_git(["remote", "set-url", "origin", clean_remote])

if push_code == 0:
    print(f"\nSUCCÈS ! Projet poussé avec succès vers : {clean_remote}")
else:
    print(f"\nÉchec du push avec le code : {push_code}")
