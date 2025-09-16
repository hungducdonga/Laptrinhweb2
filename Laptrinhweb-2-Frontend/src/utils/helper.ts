export function handleBlurChecking(field: string, value: string, setValue: (field: string, val: string) => void) {
  if (!value.trim()) {
    setValue(field, 'This field cannot be left blank.');
  } else {
    setValue(field, '');
    console.log('Input submitted:', value);
  }
}

export const handleError = (error: unknown) => {
  const errorMessage = (error as { message?: string })?.message;
  if (errorMessage) {
    console.log(errorMessage);
    return errorMessage;
  }
};
