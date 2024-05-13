package com.store.drinks.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutPageController {
  
  @Value(value = "${spring.profiles.active}")
  private String ambiente;
  
  @GetMapping("/about")
  public ModelAndView modelAndView() {
    return new ModelAndView("PageAbout").addObject("ambiente", ambiente);
  }
  
}
