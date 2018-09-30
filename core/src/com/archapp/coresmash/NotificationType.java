package com.archapp.coresmash;

public enum NotificationType {
    NULL,
    TILE_DESTROYED,
    BALL_LAUNCHED,
    TARGET_SCORE_REACHED,
    NOTIFICATION_TYPE_MOVINGTILE_COLLIDED,
    NO_COLOR_MATCH,
    SAME_COLOR_MATCH,
    NOTIFICATION_TYPE_CENTER_TILE_DESRTOYED,
    NOTIFICATION_TYPE_NEW_TILE_CREATED,
    ASTRONAUTS_FOUND,

    NOTIFICATION_TYPE_SCORE_INCREMENTED,
    NOTIFICATION_TYPE_STREAK_3X,
    NOTIFICATION_TYPE_STREAK_4X,
    NOTIFICATION_TYPE_STREAK_5X,

    TILEMAP_INITIALIZED,
    LIVES_AMOUNT_CHANGED,
    MOVES_AMOUNT_CHANGED,

    REWARDED_MOVES,
    REWARDED_LIVES,
    NOTIFICATION_TYPE_PATH_TO_CENTER_OBSTRUCTED,
    COLOR_MATCHES_FINISHED,
}
