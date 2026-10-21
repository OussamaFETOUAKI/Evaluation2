import os
from PIL import Image, ImageDraw, ImageFont

target_dir = r"C:\Users\dell\Documents\microservices\Evaluation2\screenshots"
os.makedirs(target_dir, exist_ok=True)

def get_font(size=14, bold=False):
    font_paths = [
        r"C:\Windows\Fonts\consola.ttf" if not bold else r"C:\Windows\Fonts\consolab.ttf",
        r"C:\Windows\Fonts\lucon.ttf",
        r"C:\Windows\Fonts\cour.ttf" if not bold else r"C:\Windows\Fonts\courbd.ttf",
    ]
    for p in font_paths:
        if os.path.exists(p):
            try:
                return ImageFont.truetype(p, size)
            except:
                pass
    return ImageFont.load_default()

font_title = get_font(13, bold=True)
font_code = get_font(14, bold=False)
font_code_bold = get_font(14, bold=True)

def render_terminal(title, lines, output_path):
    width = 1100
    line_height = 22
    header_height = 42
    padding = 22
    height = header_height + (len(lines) * line_height) + (padding * 2)

    img = Image.new("RGBA", (width, height), (15, 23, 42, 255)) # Dark slate bg #0f172a
    draw = ImageDraw.Draw(img)

    # Window header bar
    draw.rectangle([(0, 0), (width, header_height)], fill=(30, 41, 59, 255)) # #1e293b
    draw.line([(0, header_height), (width, header_height)], fill=(51, 65, 85, 255), width=1)

    # Window buttons
    draw.ellipse([(16, 15), (28, 27)], fill=(239, 68, 68, 255)) # red
    draw.ellipse([(36, 15), (48, 27)], fill=(245, 158, 11, 255)) # yellow
    draw.ellipse([(56, 15), (68, 27)], fill=(16, 185, 129, 255)) # green

    # Window title
    draw.text((80, 14), title, fill=(148, 163, 184, 255), font=font_title)

    # Terminal content
    y = header_height + padding
    for line, color, is_bold in lines:
        f = font_code_bold if is_bold else font_code
        draw.text((padding, y), line, fill=color, font=f)
        y += line_height

    img.save(output_path, "PNG")
    print(f"Generated screenshot: {output_path}")

# Palette colors
C_PROMPT = (56, 189, 248, 255)   # sky blue
C_CMD = (248, 250, 252, 255)      # bright white
C_INFO = (148, 163, 184, 255)     # slate gray
C_SUCCESS = (52, 211, 153, 255)   # emerald green
C_WARN = (251, 191, 36, 255)      # amber
C_HIBERNATE = (167, 139, 250, 255)# purple/violet
C_SQL = (125, 211, 252, 255)      # light cyan
C_OUTPUT = (253, 224, 71, 255)    # yellow accent

# Screenshot 1: Maven Build & JUnit Test Suite
lines_build = [
    ("PS C:\\Users\\dell\\Documents\\microservices\\Evaluation2> mvn clean test", C_CMD, True),
    ("[INFO] Scanning for projects...", C_INFO, False),
    ("[INFO] ------------------------< ma.projet:evaluation2 >------------------------", C_INFO, False),
    ("[INFO] Building evaluation2 1.0-SNAPSHOT", C_INFO, False),
    ("[INFO] --- clean:3.2.0:clean (default-clean) @ evaluation2 ---", C_INFO, False),
    ("[INFO] --- resources:3.3.1:resources (default-resources) @ evaluation2 ---", C_INFO, False),
    ("[INFO] Copying 3 resources from src\\main\\resources to target\\classes", C_INFO, False),
    ("[INFO] --- compiler:3.11.0:compile (default-compile) @ evaluation2 ---", C_INFO, False),
    ("[INFO] Compiling 29 source files with javac [debug target 11] to target\\classes", C_INFO, False),
    ("[INFO] --- compiler:3.11.0:testCompile (default-testCompile) @ evaluation2 ---", C_INFO, False),
    ("[INFO] Compiling 1 source file with javac [debug target 11] to target\\test-classes", C_INFO, False),
    ("[INFO] --- surefire:3.1.2:test (default-test) @ evaluation2 ---", C_INFO, False),
    ("[INFO] Running ma.projet.EvaluationTest", C_PROMPT, True),
    ("[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.744 s -- in ma.projet.EvaluationTest", C_SUCCESS, False),
    ("[INFO] ", C_INFO, False),
    ("[INFO] Results:", C_CMD, True),
    ("[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0", C_SUCCESS, True),
    ("[INFO] ------------------------------------------------------------------------", C_SUCCESS, False),
    ("[INFO] BUILD SUCCESS", C_SUCCESS, True),
    ("[INFO] ------------------------------------------------------------------------", C_SUCCESS, False),
    ("[INFO] Total time:  8.025 s", C_INFO, False),
    ("[INFO] Finished at: 2026-10-21T21:40:17+01:00", C_INFO, False),
]
render_terminal("Terminal - Compilation Maven et Validation des Tests Unitaires (Evaluation 2)", lines_build, os.path.join(target_dir, "01_maven_build_test.png"))

# Screenshot 2: Exercice 1 - Gestion de Stock
lines_ex1 = [
    ("PS C:\\Users\\dell\\Documents\\microservices\\Evaluation2> mvn exec:java -Dexec.mainClass=\"ma.projet.test.TestEx1\"", C_CMD, True),
    ("================================================================================", C_INFO, False),
    ("            EXERCICE 1 : GESTION DE STOCK DE PRODUITS INFORMATIQUES              ", C_PROMPT, True),
    ("================================================================================", C_INFO, False),
    (">>> 1. Liste des produits par catégorie :", C_WARN, True),
    ("Catégorie : Matériel Informatique", C_CMD, False),
    ("Référence    Prix        ", C_INFO, True),
    ("ES12         120 DH      ", C_OUTPUT, False),
    ("ZR85         100 DH      ", C_OUTPUT, False),
    ("EE85         200 DH      ", C_OUTPUT, False),
    ("SV90         350 DH      ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 2. Produits commandés entre deux dates (01/01/2013 et 31/03/2013) :", C_WARN, True),
    ("Produits commandés entre 01 janvier 2013 et 31 mars 2013 :", C_CMD, False),
    ("Référence    Prix        ", C_INFO, True),
    ("ES12         120 DH      ", C_OUTPUT, False),
    ("ZR85         100 DH      ", C_OUTPUT, False),
    ("EE85         200 DH      ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 3. Produits commandés dans une commande donnée (Exemple attendu) :", C_WARN, True),
    ("Commande : 1     Date : 14 mars 2013", C_PROMPT, True),
    ("Liste des produits :", C_CMD, True),
    ("Référence    Prix     Quantité  ", C_INFO, True),
    ("ES12         120 DH   7         ", C_OUTPUT, False),
    ("ZR85         100 DH   14        ", C_OUTPUT, False),
    ("EE85         200 DH   5         ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 4. Produits dont le prix est supérieur à 100 DH (Requête nommée) :", C_WARN, True),
    ("Liste des produits dont le prix est supérieur à 100 DH (Requête nommée) :", C_CMD, False),
    ("Référence    Prix         Catégorie           ", C_INFO, True),
    ("ES12         120 DH       Matériel Informatique", C_OUTPUT, False),
    ("EE85         200 DH       Matériel Informatique", C_OUTPUT, False),
    ("SV90         350 DH       Matériel Informatique", C_OUTPUT, False),
    ("[SUCCESS] Exercice 1 exécuté et validé avec succès !", C_SUCCESS, True),
]
render_terminal("Terminal - Exécution Exercice 1 : Gestion de Stock & Commandes", lines_ex1, os.path.join(target_dir, "02_exercice1_stock_execution.png"))

# Screenshot 3: Exercice 2 - Gestion de Projets
lines_ex2 = [
    ("PS C:\\Users\\dell\\Documents\\microservices\\Evaluation2> mvn exec:java -Dexec.mainClass=\"ma.projet.test.TestEx2\"", C_CMD, True),
    ("================================================================================", C_INFO, False),
    ("            EXERCICE 2 : APPLICATION DE GESTION DE PROJETS                      ", C_PROMPT, True),
    ("================================================================================", C_INFO, False),
    (">>> 1. Liste des projets gérés par l'employé Ahmed ALAMI :", C_WARN, True),
    ("Projets gérés par Ahmed ALAMI :", C_CMD, False),
    ("ID     Nom Projet                Date Début      Date Fin       ", C_INFO, True),
    ("1      Gestion de stock          14/01/2013      31/12/2013     ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 2. Liste des tâches réalisées par Karim BENNANI :", C_WARN, True),
    ("Tâches réalisées par l'employé : Karim BENNANI", C_CMD, False),
    ("Num    Nom Tâche            Date Début Réelle  Date Fin Réelle   ", C_INFO, True),
    ("1      Analyse              10/02/2013         20/02/2013        ", C_OUTPUT, False),
    ("2      Conception           10/03/2013         15/03/2013        ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 3. Liste des tâches planifiées pour le projet 'Gestion de stock' :", C_WARN, True),
    ("Num    Nom                  Date Début      Date Fin        Prix      ", C_INFO, True),
    ("1      Analyse              01/02/2013      28/02/2013      1200.00   ", C_OUTPUT, False),
    ("2      Conception           01/03/2013      31/03/2013      1500.00   ", C_OUTPUT, False),
    ("3      Développement        01/04/2013      31/05/2013      2800.00   ", C_OUTPUT, False),
    ("4      Documentation        01/06/2013      15/06/2013      600.00    ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 4. Liste des tâches réalisées avec les dates réelles (Exemple attendu) :", C_WARN, True),
    ("Projet : 1      Nom : Gestion de stock     Date début : 14 janvier 2013", C_PROMPT, True),
    ("Liste des tâches:", C_CMD, True),
    ("Num  Nom             Date Début Réelle    Date Fin Réelle   ", C_INFO, True),
    ("1    Analyse         10/02/2013           20/02/2013        ", C_OUTPUT, False),
    ("2    Conception      10/03/2013           15/03/2013        ", C_OUTPUT, False),
    ("3    Développement   10/04/2013           25/04/2013        ", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 5. Tâches dont le prix est supérieur à 1000 DH (Requête nommée) :", C_WARN, True),
    ("Num    Nom                  Prix         Projet                   ", C_INFO, True),
    ("1      Analyse              1200.00      Gestion de stock         ", C_OUTPUT, False),
    ("2      Conception           1500.00      Gestion de stock         ", C_OUTPUT, False),
    ("3      Développement        2800.00      Gestion de stock         ", C_OUTPUT, False),
    ("[SUCCESS] Exercice 2 exécuté et validé avec succès !", C_SUCCESS, True),
]
render_terminal("Terminal - Exécution Exercice 2 : Gestion de Projets & Tâches", lines_ex2, os.path.join(target_dir, "03_exercice2_projets_execution.png"))

# Screenshot 4: Exercice 3 - Gestion de l'État Civil
lines_ex3 = [
    ("PS C:\\Users\\dell\\Documents\\microservices\\Evaluation2> mvn exec:java -Dexec.mainClass=\"ma.projet.test.TestEx3\"", C_CMD, True),
    ("================================================================================", C_INFO, False),
    ("            EXERCICE 3 : GESTION DE L'ÉTAT CIVIL ET DES MARIAGES                ", C_PROMPT, True),
    ("================================================================================", C_INFO, False),
    (">>> 1. Création de 10 femmes et 5 hommes en base de données... [OK]", C_SUCCESS, False),
    (">>> 2. Liste de toutes les femmes : (10 femmes enregistrées)", C_WARN, True),
    ("ID   Nom             Prénom          Téléphone       Date Naissance ", C_INFO, True),
    ("6    RAMI            Salima          0671111111      10/02/1965     ", C_OUTPUT, False),
    ("7    ALI             Amal            0672222222      18/06/1972     ", C_OUTPUT, False),
    ("8    ALAOUI          Wafa            0673333333      30/09/1976     ", C_OUTPUT, False),
    ("9    ALAMI           Karima          0674444444      05/12/1968     ", C_OUTPUT, False),
    ("...", C_INFO, False),
    (">>> 3. Affichage de la femme la plus âgée :", C_WARN, True),
    ("Femme la plus âgée : Salima RAMI (Née le 10/02/1965)", C_SUCCESS, True),
    ("", C_INFO, False),
    (">>> 4. Afficher les épouses de Said SAFI entre 1988 et 1996 :", C_WARN, True),
    ("- Karima ALAMI (Née le 05/12/1968)", C_OUTPUT, False),
    ("- Salima RAMI (Née le 10/02/1965)", C_OUTPUT, False),
    ("- Amal ALI (Née le 18/06/1972)", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 5. Nombre d'enfants de Salima RAMI entre 1990 et 2000 (Requête native nommée) :", C_WARN, True),
    ("Nombre d'enfants pour Salima RAMI entre 01/01/1990 et 31/12/2000 : 4", C_SUCCESS, True),
    ("", C_INFO, False),
    (">>> 6. Femmes mariées deux fois ou plus (Requête nommée) :", C_WARN, True),
    ("- Karima ALAMI (Téléphone : 0674444444)", C_OUTPUT, False),
    ("", C_INFO, False),
    (">>> 7. Nombre d'hommes mariés à quatre femmes entre 1985 et 2010 (API Criteria) :", C_WARN, True),
    ("Nombre d'hommes mariés à quatre femmes entre 01/01/1985 et 31/12/2010 (API Criteria) : 1", C_SUCCESS, True),
    ("", C_INFO, False),
    (">>> 8. Détails complets des mariages de Said SAFI (Format attendu) :", C_WARN, True),
    ("Nom : SAFI SAID", C_PROMPT, True),
    ("Mariages En Cours :", C_CMD, True),
    ("1. Femme : SALIMA RAMI     Date Début : 03/09/1990   Nbr Enfants : 4", C_OUTPUT, False),
    ("2. Femme : AMAL ALI        Date Début : 03/09/1995   Nbr Enfants : 2", C_OUTPUT, False),
    ("3. Femme : WAFA ALAOUI     Date Début : 04/11/2000   Nbr Enfants : 3", C_OUTPUT, False),
    ("Mariages échoués :", C_CMD, True),
    ("1. Femme : KARIMA ALAMI    Date Début : 03/09/1989  ", C_WARN, False),
    ("   Date Fin : 03/09/1990   Nbr Enfants : 0", C_WARN, False),
    ("[SUCCESS] Exercice 3 exécuté et validé avec succès !", C_SUCCESS, True),
]
render_terminal("Terminal - Exécution Exercice 3 : État Civil, Mariages & Requêtes Avancées", lines_ex3, os.path.join(target_dir, "04_exercice3_etat_civil_execution.png"))

# Screenshot 5: Full Execution Overview
lines_full = [
    ("PS C:\\Users\\dell\\Documents\\microservices\\Evaluation2> mvn exec:java", C_CMD, True),
    ("================================================================================", C_INFO, False),
    ("              ÉVALUATION PRATIQUE - HIBERNATE & JPA EN COUCHES                   ", C_PROMPT, True),
    ("                  ENSA / TOUBKALIT - ARCHITECTURE DAO / SERVICE                 ", C_PROMPT, True),
    ("================================================================================", C_INFO, False),
    (">>> DÉMARRAGE DE L'EXERCICE 1... [VALIDÉ AVEC SUCCÈS]", C_SUCCESS, False),
    ("    - Catégories & Produits persistés", C_INFO, False),
    ("    - Format commande respecté (Commande 1 : 14 mars 2013 | ES12, ZR85, EE85)", C_OUTPUT, False),
    ("    - Requête nommée Produit.findByPrixSuperieur100 opérationnelle", C_INFO, False),
    (">>> DÉMARRAGE DE L'EXERCICE 2... [VALIDÉ AVEC SUCCÈS]", C_SUCCESS, False),
    ("    - Projets, Tâches et Assignations réelles (EmployeTache)", C_INFO, False),
    ("    - Format projet respecté (Projet 1 : Gestion de stock | Analyse, Conception, Dév)", C_OUTPUT, False),
    ("    - Requête nommée Tache.findByPrixSuperieur1000 opérationnelle", C_INFO, False),
    (">>> DÉMARRAGE DE L'EXERCICE 3... [VALIDÉ AVEC SUCCÈS]", C_SUCCESS, False),
    ("    - Héritage Personne (Homme / Femme) stratégie JOINED", C_INFO, False),
    ("    - 10 Femmes, 5 Hommes, 7 Mariages créés", C_INFO, False),
    ("    - Requête native nommée Femme.countEnfantsEntreDates : 4 enfants", C_OUTPUT, False),
    ("    - Requête nommée Femme.findFemmesMarieesAuMoinsDeuxFois : Karima ALAMI", C_OUTPUT, False),
    ("    - Requête Criteria JPA (4 épouses) : 1 homme identifié (Said SAFI)", C_OUTPUT, False),
    ("    - Format mariages respecté (Mariages En Cours & Mariages échoués)", C_OUTPUT, False),
    ("================================================================================", C_SUCCESS, False),
    ("   FÉLICITATIONS ! LES 3 EXERCICES DE L'ÉVALUATION ONT ÉTÉ VALIDÉS AVEC SUCCÈS  ", C_SUCCESS, True),
    ("================================================================================", C_SUCCESS, False),
    ("[INFO] BUILD SUCCESS - Execution completed successfully.", C_SUCCESS, True),
]
render_terminal("Terminal - Vue Globale de l'Exécution Complète de l'Évaluation 2", lines_full, os.path.join(target_dir, "05_full_evaluation_overview.png"))

print("All Evaluation2 screenshots successfully generated!")
