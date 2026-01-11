# Bonus: contestatii + feedback - Mihut Maria-Emilia

Vreau un modul simplu de contestatii si feedback. Studentul spune la ce intrebare are problema, de ce, si vede daca i se aproba sau nu; poate lasa si un comentariu general. 
Profesorul vede rapid unde se aduna nemultumirile si cate sunt pozitive vs negative.

## Ce adaug
- Clasa `Contestation`: student, examen, idx intrebare, motiv, status (PENDING/APPROVED/REJECTED), data.
- Clasa `FeedbackNote`: student, examen, tip (POSITIVE/NEGATIVE/NEUTRAL), comentariu, data.
- In `Exam`: metode `registerContestation`, `resolveContestation` (bonus doar pt fill/open, + 0.5 configurabil), `addFeedback`.
- In `Platform`: `writeContestationReport` si `writeFeedbackSummary` care scot fisierele cu listele si sumarul.
- Extra: Grila cu penalizare: camp `penaltyWrong` in `MultipleChoiceQuestion`, setat prin `set_penalty <exam> <value>`; la raspuns gresit scade putin din punctaj.

## Colectii
- Contestatii: `List<Contestation>` + optional `Map<String, List<Contestation>>` pe student pentru cautare rapida.
- Feedback: `List<FeedbackNote>` si `EnumMap<FeedbackType, Integer>` cand fac sumarul.

## Output (exemplu)
```
contestation_Algoritmi_03-02-2025-12-00.out
1 | qIdx=2 | student=ana | status=APPROVED | motiv="enunt ambiguu" | +0.5p (doar fill/open)

feedback_Algoritmi_03-02-2025-12-00.out
POSITIVE: 3 | NEGATIVE: 2 | NEUTRAL: 1
- ana: "imi place structura"
- ion: "prea putin timp la q4"
```
