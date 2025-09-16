import { useState } from 'react';

type State<T> = {
  [key: string]: T;
};

function useCombinedState<T>(initialState: State<T>) {
  const [state, setState] = useState<State<T>>(initialState);

  const setField = (field: string, value: T) => {
    setState((preState) => ({
      ...preState,
      [field]: value
    }));
  };
  return [state, setField] as const;
}

export default useCombinedState;
