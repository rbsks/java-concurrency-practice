package com.example.concurrency.completable;

import java.util.List;

public class MemberService {

    public List<Member> getMembers() {
        try {
            Thread.sleep(1000);
            return List.of(
                    new Member("coby", 32),
                    new Member("rbsks", 32),
                    new Member("gyubin", 32),
                    new Member("han", 32),
                    new Member("hangyubin", 32)
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
