package model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Week {
    private List<Meal> lunchList;
    private List<Meal> dinnerList;
    private int luncQnt;
    private int dinnerQnt;
}
