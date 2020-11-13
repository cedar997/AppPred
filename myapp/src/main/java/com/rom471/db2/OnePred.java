package com.rom471.db2;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class OnePred {
    @PrimaryKey(autoGenerate = true)
    long id;
    int top1;
    int top3;
    int top5;

    public OnePred() {
    }
    @Ignore
    public OnePred(int top1, int top3, int top5) {
        this.top1 = top1;
        this.top3 = top3;
        this.top5 = top5;
    }

    public long getId() {
        return id;
    }

    public int getTop1() {
        return top1;
    }

    public void setTop1(int top1) {
        this.top1 = top1;
    }

    public int getTop3() {
        return top3;
    }

    public void setTop3(int top3) {
        this.top3 = top3;
    }

    public int getTop5() {
        return top5;
    }

    public void setTop5(int top5) {
        this.top5 = top5;
    }

    @Override
    public String toString() {
        return "OnePred{" +
                "top1=" + top1 +
                ", top3=" + top3 +
                ", top5=" + top5 +
                '}';
    }
}
