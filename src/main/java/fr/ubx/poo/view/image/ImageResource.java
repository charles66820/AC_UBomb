package fr.ubx.poo.view.image;

public enum ImageResource {
    BANNER_BOMB ("banner/banner_bomb.png"),
    BANNER_RANGE ("banner/banner_range.png"),
    HEART("heart.png"),
    KEY("key.png"),
    DIGIT_0 ("banner/digit/banner_0.jpg"),
    DIGIT_1 ("banner/digit/banner_1.jpg"),
    DIGIT_2 ("banner/digit/banner_2.jpg"),
    DIGIT_3 ("banner/digit/banner_3.jpg"),
    DIGIT_4 ("banner/digit/banner_4.jpg"),
    DIGIT_5 ("banner/digit/banner_5.jpg"),
    DIGIT_6 ("banner/digit/banner_6.jpg"),
    DIGIT_7 ("banner/digit/banner_7.jpg"),
    DIGIT_8 ("banner/digit/banner_8.jpg"),
    DIGIT_9 ("banner/digit/banner_9.jpg"),
    BOMB_1 ("bomb/bomb1.png"),
    BOMB_2 ("bomb/bomb2.png"),
    BOMB_3 ("bomb/bomb3.png"),
    BOMB_4 ("bomb/bomb4.png"),
    EXPLOSION ("explosion.png"),
    PRINCE ("bomberman/bomberman.png"),
    PRINCE_DOWN("bomberman/bomberman_down.png"),
    PRINCE_LEFT("bomberman/bomberman_left.png"),
    PRINCE_RIGHT("bomberman/bomberman_right.png"),
    PRINCE_UP("bomberman/bomberman_up.png"),
    PRINCESS ("bomberwoman/bomberwoman.png"),
    PRINCESS_DOWN("bomberwoman/bomberwoman_down.png"),
    PRINCESS_LEFT("bomberwoman/bomberwoman_left.png"),
    PRINCESS_RIGHT("bomberwoman/bomberwoman_right.png"),
    PRINCESS_UP("bomberwoman/bomberwoman_up.png"),
    MONSTER_DOWN("monster/monster_down.png"),
    MONSTER_LEFT("monster/monster_left.png"),
    MONSTER_RIGHT("monster/monster_right.png"),
    MONSTER_UP("monster/monster_up.png"),
    STONE("stone.png"),
    TREE("tree.png"),
    BOX ("box.png"),
    DOOR_OPENED ("door_opened.png"),
    DOOR_CLOSED ("door_closed.png"),
    BOMB_NUMBER_INC ("bonus/bonus_bomb_nb_inc.png"),
    BOMB_NUMBER_DEC ("bonus/bonus_bomb_nb_dec.png"),
    BOMB_RANGE_INC ("bonus/bonus_bomb_range_inc.png"),
    BOMB_RANGE_DEC ("bonus/bonus_bomb_range_dec.png"),
    ;

    private final String FileName;

    ImageResource(String fileName) {
        this.FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }
}
