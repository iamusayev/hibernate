package az.hibernate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {
    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicates builder() {
        return new QPredicates();
    }

    public <T> QPredicates add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }
}
