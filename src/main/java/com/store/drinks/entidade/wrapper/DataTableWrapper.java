package com.store.drinks.entidade.wrapper;

import java.util.List;

public class DataTableWrapper<E> {

  private int draw;
  private int start;
  private long recordsTotal;
  private long recordsFiltered;
  private List<E> data;

  public int getDraw() {
    return draw;
  }

  public void setDraw(int draw) {
    this.draw = draw;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public long getRecordsTotal() {
    return recordsTotal;
  }

  public void setRecordsTotal(long recordsTotal) {
    this.recordsTotal = recordsTotal;
  }

  public long getRecordsFiltered() {
    return recordsFiltered;
  }

  public void setRecordsFiltered(long recordsFiltered) {
    this.recordsFiltered = recordsFiltered;
  }

  public List<E> getData() {
    return data;
  }

  public void setData(List<E> data) {
    this.data = data;
  }
}
