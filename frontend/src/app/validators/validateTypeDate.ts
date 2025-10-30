import { AbstractControl, FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";

export class ValidateTypeDate {
  static validateDateIsFutureFrom(): ValidatorFn {
    console.log("VALIDATOR INIT");
    return (control: AbstractControl): ValidationErrors | null => {
      if (!(control instanceof FormGroup)) {
        return null;
      }
      let missingDateError: ValidationErrors | null = null;
      let startDateFuturError: ValidationErrors | null = null;
      let endDateFuturError: ValidationErrors | null = null;
      let endDateBeforeStartDateError: ValidationErrors | null = null;
      let unknownDateError: ValidationErrors | null = null;
      let errors = {
        missingDate: missingDateError ? missingDateError : null,
        startDateFutur: startDateFuturError ? startDateFuturError : null,
        endDateFutur: endDateFuturError ? endDateFuturError : null,
        endDateBeforeStartDate: endDateBeforeStartDateError ? endDateBeforeStartDateError : null,
        unknownDateError: unknownDateError ? unknownDateError : null
      };
      const formGroup = control as FormGroup;
      const currentDate = Date.now();
      const startDate = toEpochLocal(formGroup.get('startDate')?.value);
      const endDate = toEpochLocal(formGroup.get('endDate')?.value);

      if (!startDate) {
        formGroup.get('startDate')?.setErrors({ missingDate: true });
        formGroup.get('endDate')?.setErrors({ missingDate: true });
        missingDateError = { missingDate: true };
      }
      if (!endDate) {
        formGroup.get('endDate')?.setErrors({ missingDate: true });
        missingDateError = { missingDate: true };
      }
      if (startDate) {
        const startDateError = startDate >= currentDate ? null : { startDateNotInFutur: true };
        formGroup.get('startDate')?.setErrors(startDateError);
        startDateFuturError = startDateError;
      }
      if (endDate && !startDate) {
        const endDateError = endDate >= currentDate ? null : { endDateNotInFuture: true };
        formGroup.get('endDate')?.setErrors(endDateError);
        endDateFuturError = endDateError;
      }
      if (endDate && startDate) {
        const endBeforeStartError = endDate >= startDate ? null : { endDateBeforeStartDate: true };
        formGroup.get('endDate')?.setErrors(endBeforeStartError);
        endDateBeforeStartDateError = endBeforeStartError;
      }

      const hasAnyError = Object.values(errors).some(v => v !== null);
      return hasAnyError ? (errors as ValidationErrors) : null;
    };
  }
}

function toEpochLocal(datetimeLocal: string | null | undefined): number {
    if (!datetimeLocal) return NaN;
    // datetimeLocal attendu: "YYYY-MM-DDTHH:mm" ou "YYYY-MM-DDTHH:mm:ss"
    const [datePart, timePart] = datetimeLocal.split('T');
    if (!datePart) return NaN;
    const [year, month, day] = datePart.split('-').map(Number);
    const timeSegments = (timePart || '').split(':').map(Number);
    const hour = timeSegments[0] ?? 0;
    const minute = timeSegments[1] ?? 0;
    const second = timeSegments[2] ?? 0;
    // new Date(year, monthIndex, day, hour, minute, second) -> crée une Date en heure locale
    return new Date(year, (month ?? 1) - 1, day ?? 1, hour, minute, second).getTime();
  }
