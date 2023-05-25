package com.university.iibackend.service;

import com.university.iibackend.model.Chapter;
import com.university.iibackend.model.dto.ChapterListingItem;
import com.university.iibackend.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepo;

    public List<ChapterListingItem> findListingChapters() {
        return chapterRepo.findAll().stream()
                .map(chapter -> new ChapterListingItem(chapter.getId(), chapter.getTitle()))
                .toList();
    }

    public Optional<Chapter> findChapterById(Integer id) {
        return chapterRepo.findById(id);
    }

    public void saveChapter(Chapter item) {
        chapterRepo.save(item);
    }

    public void deleteAllChapters() {
        chapterRepo.deleteAll();
    }
}
