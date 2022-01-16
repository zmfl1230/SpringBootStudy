package hello.hellospring.learningtest.singleton;
/**
 * 상태를 갖는 클래스를 싱글톤 오브젝트로 만들었을 때의 위험을 테스트 해보기 위한 클래스
 * */

public class StatefulService {
    private int price;

    public void order(String product, int price) {
        System.out.println("product = " + product + ", price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
