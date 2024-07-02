package br.chronos.query;

import br.util.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("unchecked")
public class GenericSpesification<T> implements Specification<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(GenericSpesification.class);

    @SuppressWarnings("java:S1948")
    private Set<SearchCriteria> list = new HashSet<>();

    private Boolean distinct = false;

    public Set<SearchCriteria> getList() {
        return list;
    }

    public void setList(Set<SearchCriteria> list) {
        this.list = list;
    }

    /**
     * @param list
     */
    public GenericSpesification(Set<SearchCriteria> list) {
        super();
        this.list = list;
    }

    public GenericSpesification(Set<SearchCriteria> list, Boolean distinct) {
        super();
        this.list = list;
        this.distinct = distinct;
    }

    public GenericSpesification() {
        super();
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        query.distinct(distinct);

        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : list) {
            if (criteria.getKey().contains(".") && !criteria.getKey().contains(" OR ")) {
                Join<Object, Object> join = createJoin(root, criteria.getKey());
                if (join != null) {
                    criteria.setKey(getLastKey(criteria.getKey()));
                    addPredicate(join, builder, predicates, criteria);
                }
            } else {
                addPredicate(root, builder, predicates, criteria);
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Join<Object, Object> createJoin(Root<T> root, String key) {
        Join<Object, Object> join = null;
        String[] split = key.split("\\.");
        for (int i = 0; i < split.length - 1; i++) {
            String field = split[i];
            if (join != null)
                join = join.join(field);
            else
                join = root.join(field);
        }
        return join;
    }

    private String getLastKey(String key) {
        String[] split = key.split("\\.");
        return split[split.length - 1];
    }

    @SuppressWarnings("java:S131")
    private void addPredicate(Join<Object, Object> join, CriteriaBuilder builder, List<Predicate> predicates, SearchCriteria criteria) {
        Class<? extends Object> javaType = join.get(criteria.getKey()).getJavaType();
        this.getEnumValue(javaType, criteria);

        switch (criteria.getOperation()) {
            case GREATER_THAN:
                predicates.add(builder.greaterThan(join.get(criteria.getKey()), criteria.getValue().toString()));
                break;
            case LESS_THAN:
                predicates.add(builder.lessThan(join.get(criteria.getKey()), criteria.getValue().toString()));
                break;
            case GREATER_THAN_EQUAL:
                predicates.add(builder.greaterThanOrEqualTo(join.get(criteria.getKey()), criteria.getValue().toString()));
                break;
            case LESS_THAN_EQUAL:
                predicates.add(builder.lessThanOrEqualTo(join.get(criteria.getKey()), criteria.getValue().toString()));
                break;
            case NOT_EQUAL:
                predicates.add(builder.notEqual(join.get(criteria.getKey()), criteria.getValue()));
                break;
            case EQUAL:
                predicates.add(builder.equal(join.get(criteria.getKey()), criteria.getValue()));
                break;
            case MATCH:
                predicates.add(builder.like(builder.lower(join.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                break;
            case MATCH_START:
                predicates.add(builder.like(builder.lower(join.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
                break;
            case MATCH_END:
                predicates.add(builder.like(builder.lower(join.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase()));
                break;
            case BETWEEN_DATE:
                if (javaType.equals(LocalDateTime.class)) {
                    Path<LocalDateTime> entityDate = join.get(criteria.getKey());
                    predicates.add(builder.between(entityDate, getComparingDateTime((ArrayList<String>) criteria.getValue(), 0), getComparingDateTime((ArrayList<String>) criteria.getValue(), 1)));
                } else if (javaType.equals(LocalDate.class)) {

                    Path<LocalDate> entityDate = join.get(criteria.getKey());
                    predicates.add(builder.between(entityDate, getComparingDate((ArrayList<String>) criteria.getValue(), 0), getComparingDate((ArrayList<String>) criteria.getValue(), 1)));
                }

                break;
            case IN:
                Path<Object> expression = join.get(criteria.getKey());
                Predicate predicate = null;

                if (javaType.equals(Long.class)) {

                    predicate = expression.in((ArrayList<Long>) criteria.getValue());

                } else {

                    predicate = expression.in(criteria.getValue());

                }

                predicates.add(predicate);
                break;
        }
    }

    @SuppressWarnings({"java:S6541", "java:S3776"})
    private void addPredicate(Root<?> root, CriteriaBuilder builder, List<Predicate> predicates, SearchCriteria criteria) {
        try {
            if (!criteria.getOperation().equals(SearchOperation.OR)) {
                Class<? extends Object> javaType = root.get(criteria.getKey()).getJavaType();
                this.getEnumValue(javaType, criteria);
            }

            switch (criteria.getOperation()) {
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case EQUAL:
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case MATCH:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_END:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase()));
                    break;
                case BETWEEN_DATE:
                    Class<? extends Object> javaType = root.get(criteria.getKey()).getJavaType();
                    this.getEnumValue(javaType, criteria);
                    if (javaType.equals(LocalDateTime.class)) {
                        Path<LocalDateTime> entityDate = root.get(criteria.getKey());
                        predicates.add(builder.between(entityDate, getComparingDateTime((ArrayList<String>) criteria.getValue(), 0), getComparingDateTime((ArrayList<String>) criteria.getValue(), 1)));
                    } else if (javaType.equals(LocalDate.class)) {

                        Path<LocalDate> entityDate = root.get(criteria.getKey());
                        predicates.add(builder.between(entityDate, getComparingDate((ArrayList<String>) criteria.getValue(), 0), getComparingDate((ArrayList<String>) criteria.getValue(), 1)));
                    }

                    break;
                case IN:
                    Path<Object> expression = root.get(criteria.getKey());
                    Predicate predicate = expression.in(criteria.getValue());
                    predicates.add(predicate);
                    break;
                case OR:
                    String[] splits = criteria.getKey().split(" OR ");

                    Predicate p1 = null;
                    Predicate p2 = null;
                    for (int i = 0; i < splits.length; i++) {
                        String split = splits[i];

                        if (split.contains(".")) {
                            Join<Object, Object> join = null;

                            String[] fields = splits[i].split("\\.");

                            for (int j = 0; j < fields.length; j++) {
                                String field = fields[j];
                                criteria.setKey(field);
                                if ((fields.length - 1) != j) {
                                    if (join != null) {
                                        join = join.join(field, JoinType.LEFT);
                                    } else {
                                        join = root.join(field, JoinType.LEFT);
                                    }
                                }
                            }

                            if (join != null) {
                                if (i == 0) {
                                    p1 = builder.like(builder.lower(join.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
                                } else {
                                    p2 = builder.like(builder.lower(join.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
                                }
                            }

                        } else {
                            criteria.setKey(i == 0 ? splits[0] : splits[1]);
                            if (i == 0) {
                                p1 = builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
                            } else {
                                p2 = builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
                            }
                        }
                    }
                    predicates.add(builder.or(p1, p2));
                    break;
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    private LocalDateTime getComparingDateTime(ArrayList<String> dates, int order) {
        LocalDateTime result = null;
        if (order == 0) {
            LocalDateTime from = DateConverter.stringTimeToLocalDateTime(dates.get(0));
            result = from;
        } else {
            LocalDateTime to = DateConverter.stringTimeToLocalDateTime(dates.get(1));
            result = to;
        }
        return result;
    }

    private LocalDate getComparingDate(ArrayList<String> dates, int order) {
        LocalDate result = null;
        if (order == 0) {
            LocalDate from = DateConverter.stringToLocalDate(dates.get(0), "yyyy-MM-dd");
            result = from;
        } else {
            LocalDate to = DateConverter.stringToLocalDate(dates.get(1), "yyyy-MM-dd");
            result = to;
        }
        return result;
    }

    private void getEnumValue(Class<? extends Object> javaType, SearchCriteria criteria) {
        if (javaType.isEnum()) {
            Object[] enumConstants = javaType.getEnumConstants();

            if (criteria.getValue() instanceof List) {
                List<Object> filteredValues = Arrays.stream(enumConstants)
                        .filter(v -> ((List<String>) criteria.getValue()).contains(v.toString()))
                        .toList();
                criteria.setValue(filteredValues);
                criteria.setOperation(SearchOperation.IN);
            } else {
                Object enumValue = Arrays.stream(enumConstants)
                        .filter(v -> v.toString().equals(criteria.getValue()))
                        .findFirst().orElse(null);
                if (enumValue != null) {
                    criteria.setValue(enumValue);
                    criteria.setOperation(SearchOperation.EQUAL);
                }
            }
        }
    }

}
