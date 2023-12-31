package com.bot.categorytree.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Entity сущность для удобства работы
 * в контексте при выгрузке excel documenta
 */

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bot_contexts")
public class BotContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "context_id")
    long id;

    @Column(name = "chat_id", nullable = false)
    long chatId;

    @Column(name = "excel_in_download")
    boolean excelInDownload;
}
