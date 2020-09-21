package com.withpet.walk;

public class Walk_ReplyUpload {
    private String reply;
    private int board_nb;
    private String walk_uid;

    public String getWalk_uid() {
        return walk_uid;
    }

    public void setWalk_uid(String walk_uid) {
        this.walk_uid = walk_uid;
    }


    Walk_ReplyUpload(int board_nb, String reply){
        this.board_nb = board_nb;
        this.reply = reply;
    }
    Walk_ReplyUpload(int board_nb, String reply, String walk_uid){
        this.board_nb = board_nb;
        this.reply = reply;
        this.walk_uid = walk_uid;
    }
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getBoard_nb() {
        return board_nb;
    }

    public void setBoard_nb(int board_nb) {
        this.board_nb = board_nb;
    }


}
