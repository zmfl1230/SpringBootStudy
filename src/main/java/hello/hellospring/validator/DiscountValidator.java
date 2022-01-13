package hello.hellospring.validator;

import hello.hellospring.CommonConstant;
import hello.hellospring.domain.Grade;
import hello.hellospring.domain.Member;
import hello.hellospring.exception.ExceptionMessage;
import hello.hellospring.exception.NotPermissionToDiscount;

public class DiscountValidator {

    public static void validateMemberGrade(Member member) {
        if (member.getGrade() != Grade.VIP)
            throw new NotPermissionToDiscount(ExceptionMessage.UNAUTHORIZED_GRADE);
    }

    public static void validatePurchasePrice(int price) {
        if (price <= CommonConstant.STANDARD_PRICE)
            throw new NotPermissionToDiscount(ExceptionMessage.NOT_ENOUGH_PRICE_FOR_DISCOUNT);
    }
}
