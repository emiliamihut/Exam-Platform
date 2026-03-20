# Platforma de examene – README
Mihut Maria-Emilia

## Descriere
Aplicatia gestioneaza examene pe baza unor comenzi din fisiere. Retine examene, intrebari si studenti, verifica raspunsuri si genereaza fisiere `.out` (print exam, istoric intrebari, raport, plus un fisier de consola). Nu am folosit tablouri clasice, doar colectii Java. Pentru timp folosesc `LocalDateTime` si un formatter comun `dd-MM-yyyy-HH-mm`.

## Clase

### Platform
- primeste comenzi din main si decide ce se intampla.

- tine examenele in `HashMap<String, Exam>`  
  am ales HashMap pt ca gasesc rapid un examen dupa nume si nu-mi trebuie sortare aici
- tine studentii in `HashMap<String, Student>`  
  aceeasi idee, caut dupa nume

- `create_exam` -> creeaza un `Exam` cu start/end
- `add_question` -> creeaza tipul de intrebare corect si o adauga in examen
- `list_questions_history` -> ia intrebarile si le afiseaza in ordinea “istoric”
- `print_exam` -> afiseaza intrebarile sortate pt varianta finala a examenului
- `register_student` -> adauga un student
- `submit_exam` -> calculeaza nota pt student pe examen
- `show_student_score` / `generate_report` -> afiseaza scoruri

### Exam
Reprezinta un examen. Are nume, start, end si tine intrebari + scoruri.

- `ArrayList<Question> questions`  
  am ales ArrayList pt ca adaug intrebari una cate una si e rapid, plus ca sortarea si accesul pe index sunt bune
- `LinkedHashMap<String, Double> scores`  
  aici as fi putut folosi si HashMap, dar am ales LinkedHashMap ca pastreaza ordinea in care au intrat scorurile

Metode importante:
- `addQuestion`
- `getQuestionHistorySorted` (sortare dupa data, apoi autor)
- `getPrintExamQuestionsSorted` (sortare dupa dificultate, apoi text)
- `setStudentScore`, `getStudentScore`
- `getStudentScoresAlphabetical` (pt raport, fac o copie si sortez dupa nume)

### Question (abstracta)
Baza pt toate intrebarile. Are campuri comune: text, autor, data, dificultate, punctaj maxim.  
Are metoda generica `checkAnswer(T)` (la grila e `ResponseOption`, la restu e `String`). Fiecare tip de intrebare isi face singura verificarea.

### MultipleChoiceQuestion
Raspuns corect = `ResponseOption` (A/B/C/D).

### OpenEndedQuestion
Raspuns corect = `String`. Accepta corect exact sau partial (aprox la lungime si daca se suprapune continutul).

### FillInTheBlankQuestion
Raspuns scurt = `String`. Accepta corect exact sau partial (±2 caractere si substring).

### Student
Tine nume, grupa si scoruri pe examene.
- `HashMap<String, Double> scoresByExam`  
  HashMap pt ca doar caut dupa numele examenului, nu ma intereseaza ordinea

### SubmissionOutsideTimeIntervalException
O exceptie folosita cand cineva da submit in afara intervalului. Retine timestamp si nume student.

### Enum-uri
- `Correctness`: CORRECT / PARTIAL / INCORRECT
- `ResponseOption`: A / B / C / D

## Sortari folosite
- la history: dupa data adaugarii, apoi dupa autor
- la print_exam: dupa dificultate, apoi dupa text
- la raport: sortare alfabetica dupa nume student
