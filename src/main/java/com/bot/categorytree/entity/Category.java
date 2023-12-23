package com.bot.categorytree.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Entity сущность категорий дерева
 * для работы с БД
 */

@Entity
@Table(name = "categories")
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@ToString
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category",
            referencedColumnName = "category_id")
    Category parrentCategory;

    @Column(name = "category_name", nullable = false, length = 30)
    String categoryName;

    @Column(name = "callback", nullable = false, length = 200)
    String callback;
    @Column(name = "back_callback", nullable = false, length = 200)
    String backCallback;

}
