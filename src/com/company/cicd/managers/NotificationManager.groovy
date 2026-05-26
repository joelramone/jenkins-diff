package com.company.cicd.managers

class NotificationManager implements Serializable {
  private final def steps

  NotificationManager(def steps) {
    this.steps = steps
  }

  void traceability(String message) {
    steps.trazabilidad(message)
  }
}
