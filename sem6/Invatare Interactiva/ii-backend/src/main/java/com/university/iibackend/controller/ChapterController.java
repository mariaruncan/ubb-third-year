package com.university.iibackend.controller;

import com.university.iibackend.model.Chapter;
import com.university.iibackend.model.User;
import com.university.iibackend.model.dto.ChapterListingItem;
import com.university.iibackend.service.ChapterService;
import com.university.iibackend.service.QuizAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
@CrossOrigin
public class ChapterController {
    private final ChapterService service;
    private final QuizAnswerService ansService;

    @GetMapping
    public ResponseEntity<List<ChapterListingItem>> getChapters() {
        return ResponseEntity.ok(service.findListingChapters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getEmployeesById(@PathVariable Integer id) {
        Optional<Chapter> chapter = service.findChapterById(id);

        boolean showArticle;
        if (chapter.isPresent()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User user) {
                int maxScoreForChapter = ansService.getMaxScoreOnQuizForUser(user, chapter.get().getQuiz());
                showArticle = maxScoreForChapter == 3;
            } else {
                showArticle = false;
            }
        } else {
            showArticle = false;
        }

        return chapter
                .map(it -> {
                            it.setShowArticle(showArticle);
                            return ResponseEntity.ok(it);
                        }
                )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
