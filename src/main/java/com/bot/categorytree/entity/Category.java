package com.bot.categorytree.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    long id;

    @Column(name = "chat_id", nullable = false)
    long chatId;

    @Column(name = "level_of_nesting")
    long levelOfNesting;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category",
            referencedColumnName = "category_id")
    Category parrentCategory;

    @Column(name = "category_name", nullable = false, length = 30)
    String categoryName;

}
