package com.RACTEC.CHATAPI.DTO;                    // Same package as before.

import com.RACTEC.CHATAPI.Model.MessageStatus;     // The status enum (ACTIVE, PINNED, DELETED).

import java.time.LocalDateTime;                    // The date-time type for createdAt.

public record MessageResponse(Long id,             // Message id.
                              String content,      // The text. Can be null for a picture-only message.
                              MessageStatus status,// ACTIVE, PINNED or DELETED.
                              LocalDateTime createdAt, // When it was sent.
                              int editCount,       // How many times it was edited.
                              Long senderId,       // Who sent it (id only, never the whole user).
                              String senderUsername, // Who sent it (name, for display).
                              String fileName,     // NEW: the random UUID name. The page uses it in GET /api/files/{fileName}. null = no picture.
                              String originalFileName) { // NEW: the user's own file name, for display only. null = no picture.
}