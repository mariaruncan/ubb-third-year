package com.university.iibackend.controller;

import com.university.iibackend.model.Chapter;
import com.university.iibackend.model.Question;
import com.university.iibackend.model.Quiz;
import com.university.iibackend.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setup")
@RequiredArgsConstructor
@CrossOrigin
public class SetupController {
    private final ChapterRepository chapterRepository;

    @PostMapping
    public void setupDatabase() {
        // quiz 1
        Question q1 = new Question(1, "Modelul Adler-Gahadar are 4 etape de evoluție a corporațiilor mulinaționale:", List.of("Etapa globală sau transațională", "Etapa produsului principal", "Etapa metodelor culturale"), 0);
        Question q2 = new Question(2, "Cultura este formata din:", List.of("Valori", "Afectivitate", "Convingere"), 0);
        Question q3 = new Question(3, "Caracteristicile comunicării eficiente sunt:", List.of("Expertiza, credibilitatea", "Teoria atitudinilor", "Teoria echilibrului"), 0);
        Quiz quiz1 = new Quiz(1, List.of(q1, q2, q3));
        // chapter 1
        Chapter chapter1 = Chapter.builder()
                .id(1)
                .title("Cultura organizațională")
                .pdfUrl("chapter/pdf/chapter1.pdf")
                .videoUrl("https://www.youtube.com/watch?v=4cBN8xH-5Qw")
                .articleUrl("chapter/pdf/article1.pdf")
                .quiz(quiz1)
                .build();
        chapterRepository.save(chapter1);

        // quiz 2
        Question q4 = new Question(4, "În organizațiile cu o orientare spre un sistem organizatori, managerii preferă o structura bazata pe:", List.of("Formalizarea rolurilor", "Relații sociale", "Instrumentaliști"), 0);
        Question q5 = new Question(5, "Organizațiile folosesc pentru coordonare următoarele mecanisme:",List.of("Vârful strategiei", "Tehnostructura", "Standardizarea proceselor de munca"), 2);
        Question q6 = new Question(6, "Climatul organizațional este definit ca:", List.of("Schimbare organizaționala", "Focus-grup", "Percepțiile obișnuite sau împărtășite între membrii unei organizații cu privire la politici, practicile si procedurile"), 2);
        Quiz quiz2 = new Quiz(2, List.of(q4, q5, q6));
        // chapter 2
        Chapter chapter2 = Chapter.builder()
                .id(2)
                .title("Diferențele culturale și organizațiile")
                .pdfUrl("chapter/pdf/chapter2.pdf")
                .videoUrl("https://www.youtube.com/watch?v=wUiEpQOAZK8")
                .articleUrl("chapter/pdf/article2.pdf")
                .quiz(quiz2)
                .build();
        chapterRepository.save(chapter2);

        // quiz 3
        Question q7 = new Question(7, "Tehnicile utilizate in cercetarea calitativa sunt:", List.of("Interviul, studiul de caz", "Experimentul", "Cvasi-experimentul"), 0);
        Question q8 = new Question(8, "Modelul lui Weisbord (1976) propune urmatoarele scopuri:", List.of("Relații, structura", "Planificare redusa", "Costuri ridicate"), 0);
        Question q9 = new Question(9, "Modelul diagnoza Sharpe- Image curprinde:", List.of("Aplicarea modelelor teoretice", "Standardul de performanta", "Compararea profilului"), 0);
        Quiz quiz3 = new Quiz(3, List.of(q7, q8, q9));
        // chapter 3
        Chapter chapter3 = Chapter.builder()
                .id(3)
                .title("Diagnoza culturii organizaționale")
                .pdfUrl("chapter/pdf/chapter3.pdf")
                .videoUrl("https://.youtube.com/watch?v=NmI-iPqaIC4")
                .articleUrl("chapter/pdf/article3.pdf")
                .quiz(quiz3)
                .build();
        chapterRepository.save(chapter3);

        // quiz 4
        Question q10 = new Question(10, "Mecanismele generatoare ale schimbarii in cadrul teoriilor dialectice care explica procesele de schimbare organizationala sunt:", List.of("selectia pe baza de competitivitate si resursele limitate", "programele si reglementarile inerente organizatiei", "pluralismul, confruntarea si conflictul"), 2);
        Question q11 = new Question(11, "Schimbarea emergenta a culturii organizationale", List.of("se bazeaza pe natura dinamica si impredictibila a schimbarii si sustine ca schimbarile nu sunt mereu controlate", "se bazeaza pe teorii construite stiintific si verificate empiric si se bazeaza pe un process amplu si rational de analiza", "implica masurarea permanenta a rezultatelor"), 0);
        Question q12 = new Question(12, "... este definita ca un set de teorii, valori, strategii si tehnici comportamentale, fundamentate stiintific, avand ca scop schimbarea planificata a locului de munca, cu scopul dezvoltarii individuale si imbunatatirii performantei organizationale, prin schimbarea comportamentului membrilor organizatiei la locul de munca.", List.of("Diversitatea", "Dezvoltarea organizationala", "Teoria campului"), 1);
        Quiz quiz4 = new Quiz(4, List.of(q10, q11, q12));
        // chapter 4
        Chapter chapter4 = Chapter.builder()
                .id(4)
                .title("Schimbare și dezvoltare organizațională")
                .pdfUrl("chapter/pdf/chapter4.pdf")
                .videoUrl("https://www.youtube.com/watch?v=0jvCTFIvjG4")
                .articleUrl("chapter/pdf/article4.pdf")
                .quiz(quiz4)
                .build();
        chapterRepository.save(chapter4);

        // quiz 5
        Question q13 = new Question(13, "Tema constanta identificata in multiplele perspective care conceptualizeaza inovatia este:", List.of("introducerea a ceva nou si util", "considerarea creativitatii ca fiind punctul de start", "accentul ferm pus pe procesul implementarii"), 0);
        Question q14 = new Question(14, "Avand in vedere observatiile desprinse din Programul de cercetare a inovatiei Minnesota, urmatoarea componenta NU face parte din perioada de dezvoltare a inovatiei:", List.of("proliferarea ideii", "determinarea prin “socuri” interne sau externe", "dezvoltarea relatiilor cu alte organizatii si modelarea cursului acestora"), 1);
        Question q15 = new Question(15, "In organizatiile organice:", List.of("predomina comunicarea pe verticala", "puterea si autoritatea se bazeaza pe nivelul ocupat in ierarhia organizationala", "exista o structura plata, cu grupuri de munca organizate temporar, in functie de diferite proiecte"), 2);
        Quiz quiz5 = new Quiz(5, List.of(q13, q14, q15));
        // chapter 5
        Chapter chapter5 = Chapter.builder()
                .id(5)
                .title("Inovația organizațională")
                .pdfUrl("chapter/pdf/chapter5.pdf")
                .videoUrl("https://www.youtube.com/watch?v=xiB3FchwtGU")
                .articleUrl("chapter/pdf/article5.pdf")
                .quiz(quiz5)
                .build();
        chapterRepository.save(chapter5);

        // quiz 6
        Question q16 = new Question(16, "Item-ul “Trebuie sa realizez diferite activitati de munca, ce in alte conditii ar trebui indeplinite intr-un mod diferit” masoara variabila:", List.of("conflict de rol", "ambuguitate de rol", "supraincarcarea rolului"), 0);
        Question q17 = new Question(17, "Testul ... NU masoara potrivirea dintre individ si organizatie.", List.of("Organizational Culture Profile (OCP)", "Organizational Curlture Assessment Instrument (OCAI)", "First Organizational Climate/Cultutre Unified Survey (FOCUS – 93)"), 2);
        Question q18 = new Question(18, "Urmatoarea afirmatie este adevarata:", List.of("potrivirea dintre individ si organizatie coreleaza pozitiv cu experienta de munca", "potrivirea dintre individ si organizatie coreleaza pozitiv cu angajamentul normativ fata de organizatie si satisfactia profesionala", "potrivirea dintre individ si organizatie coreleaza pozitiv cu intentie de a parasi organizatia"), 1);
        Quiz quiz6 = new Quiz(6, List.of(q16, q17, q18));
        // chapter 6
        Chapter chapter6 = Chapter.builder()
                .id(6)
                .title("Instrumente psihologice de măsurare a diferitelor variabile organizaționale")
                .pdfUrl("chapter/pdf/chapter6.pdf")
                .videoUrl("https://www.youtube.com/watch?v=oqFoy8ehzuc ")
                .articleUrl("chapter/pdf/article6.pdf")
                .quiz(quiz6)
                .build();
        chapterRepository.save(chapter6);

        // quiz 7
        Question q19 = new Question(19, "Tipul de cultură care influențează cel mai mult activitățile de management al cunoștințelor este: ", List.of("Cultura ierarhică", "Cultura adhocrației", "Cultura birocratică"), 1);
        Question q20 = new Question(20, "... reprezintă un mod sau stil de conducere axat pe  dezvoltarea viziunilor, valorilor și obiectivelor pe termen lung, care implică, de asemenea,  convingerea lucrătorilor să se atașeze de ele și să lucreze pentru a le atinge.", List.of("Leadership-ul transformațional", "Managementul cunoștințelor", "Cultura organizațională"), 0);
        Question q21 = new Question(21, "Tipologia culturii organizaționale introdusă de Cameron și Quinn (2006) evidențiază distincția dintre cultura adhocrației, cea a „clanului”, cea ierarhică și cea axată pe piață. Care este diferența dintre cultura „clanului” și cea axată pe piață?", List.of("Cultura „clanului” este concentrată pe exterior, iar cea axată pe piață este preocupată de facilitarea colaborării membrilor personalului.", "Cultura „clanului” este concentrată pe mediul de afaceri și este adaptabilă la reguli și norme clar definite, iar cea axată pe piață este flexibilă și adaptabilă.", "Cultura „clanului” este o cultură flexibilă, adaptabilă, concentrată pe interior, iar cea axată pe piață este orientată înspre exterior, accentul fiind pus pe stabilitate și continuitate. "), 2);
        Quiz quiz7 = new Quiz(7, List.of(q19, q20, q21));
        // chapter 7
        Chapter chapter7 = Chapter.builder()
                .id(7)
                .title("Conducere, Organizare. Managementul culturii și Management de cunoștințe.")
                .pdfUrl("chapter/pdf/chapter7.pdf")
                .videoUrl("https://www.youtube.com/watch?v=60O2OH7mHys")
                .articleUrl("chapter/pdf/article7.pdf")
                .quiz(quiz7)
                .build();
        chapterRepository.save(chapter7);
    }
}
