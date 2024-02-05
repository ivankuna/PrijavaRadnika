package hr.betaSoft.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Data<T> {

    private String name;

    private String field;

    private String model;

    private String id;

    private String option;
}