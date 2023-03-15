# RPA_final_project

Enunt
	Exista un fisier in care se tin datele fiecarei masini, nr inmatriculare, data ultimului, itp, numele si adresa de email a soferului principal. 
	Robotelul verifica la rulare fisierul, si notifica toti soferii carora le expira itp-ul in urmatoarele 2w. Fiecare adresa de mail primeste doua 
	mailuri:primul de notificare, al doilea (dupa o saptaman) ca si reminder

Puncte de atins
- Development 	
	- 2p - REF
	- 1p - custom activities, multiple workflows
	- 1p - business exceptions ??? nu prea avem
	- 1p - project documentation(PDD-ul)
	- 1p - task-ul dat de mentor(avem deja punctajul :))
- Project Presentation
	- 1p - prezentare generala cu accent pe beneficiile automatizarii
	- 1p - argumente pentru alegerea acestei implementari
	- 1p - demo cu 2-3 scenarii (1 scenariu cu exceptie)
	- 1p - raspuns la intrebari(cel mai probabil nu vor fi)

Ce am discutat cu mentorul
- putem folosi baza de date in loc de fisier(cal mai probabil nu)
- in functie de vechimea masinii => tinem in tabel si anul fabricarii masinii
	- 25+ ani - de dus la rabla
	- 10+ ani - ITP anual
	- < 10 ani - ITP la 2 ani
- exceptii pot fi: masina veche 25+, ITP ul vechi e expirat(putem sa nu mai trimitem daca e trecut termenul) + todo(find others)
- PDD - descriere amanuntita a ce si cum se intampla(vezi template)
- REF - Robotic Enterprice Framework
