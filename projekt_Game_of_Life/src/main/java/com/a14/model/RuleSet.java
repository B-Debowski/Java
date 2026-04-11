package com.a14.model;
import java.util.Set;

public record RuleSet(String name, Set<Integer> born, Set<Integer> survive) {
}
