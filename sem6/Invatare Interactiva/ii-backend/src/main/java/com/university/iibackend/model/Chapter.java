package com.university.iibackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_chapter")
public class Chapter {
    @Id
    private Integer id;
    private String title;
    private String pdfUrl;
    private String articleUrl;
    private String videoUrl;
    private Boolean showArticle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;
}
