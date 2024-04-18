import "../styles/main.css";
import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { ControlledInput } from "./ControlledInput";
import { REPLFunction } from "./REPLFunction";
import { addFunc } from "./REPLFunction";

interface REPLInputProps {
  history: any[];
  setHistory: Dispatch<SetStateAction<any[]>>;
  verbose: boolean;
  setVerbose: Dispatch<SetStateAction<boolean>>;
  setKeywordSearch: (keyword: string) => void;
}

interface CommandResult {
  command: string;
  result: string | string[][];
}
/**
 *
 * @param props contains fields
 * history - array to hold history information
 * setHistory - function that sets the history array
 * verbose -  true if verbose mode is on, false otherwise
 * setVerbose - function that sets the verbose boolean
 * @returns repl input command box
 */
export function REPLInput(props: REPLInputProps) {
  const [commandString, setCommandString] = useState<string>("");
  const inputRef = useRef<HTMLInputElement>(null); // Ref for focusing on input

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.ctrlKey && event.key === "\\") {
        event.preventDefault();
        inputRef.current?.focus();
      } else if (event.ctrlKey && event.key === "Enter") {
        event.preventDefault();
        handleSubmit(commandString);
      }
    };

    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [commandString]);

  const searchRedlining: REPLFunction = async (
    args: string[]
  ): Promise<string | string[][]> => {
    const keyword = args.join(" ");
    if (keyword === "") {
      return "Please provide a keyword";
    }
    props.setKeywordSearch(keyword);
    return `Searching for: "${keyword}": results overlayed in purple`;
  };

  const clearRedliningSearch: REPLFunction = async (
    args: string[]
  ): Promise<string | string[][]> => {
    const keyword = args.join(" ");
    props.setKeywordSearch("");
    return "Successfully cleared search results";
  };

  // map containing the commands and their functions
  var funcMap = new Map();
  addFunc(funcMap, "search", searchRedlining);
  addFunc(funcMap, "reset", clearRedliningSearch);

  /**
   *
   * @param commandString the command entered by the user into the input box
   * @param verbose whether the repl is in verbose mode or not
   * @returns an array where the first element is the command and the second is the result
   * either a string or a string[][] depending on the command
   */
  async function getResult(
    commandString: string,
    verbose: boolean
  ): Promise<CommandResult> {
    var result;
    if (commandString === "mode") {
      if (verbose) {
        result = "mode changed to brief";
      } else {
        result = "mode changed to verbose";
      }
      props.setVerbose(!verbose);
      return { command: commandString, result };
    } else {
      const args = commandString.split(" ");
      const command = args.shift();
      const func = funcMap.get(command);

      if (func) {
        try {
          // Await the async function corresponding to the command
          const result = await func(args);
          return { command: commandString, result };
        } catch (error) {
          return {
            command: commandString,
            result: (error as Error).message,
          };
        }
      } else {
        return {
          command: commandString,
          result: "Not a valid command, please try again",
        };
      }
    }
  }

  /** Sets history array to include the newest command and result and resets the
   * command string to ""
   *
   * @param commandString the command entered by the user into the input box
   * @return a command box and submit button
   */
  async function handleSubmit(commandString: string) {
    if (commandString === "clear") {
      props.setHistory((prevHistory) => []);
    } else {
      const result = await getResult(commandString, props.verbose);
      props.setHistory((prevHistory) => [...prevHistory, result]);
    }

    setCommandString("");
  }

  return (
    <div className="repl-input" aria-label="Command Prompt Area">
      <fieldset>
        <legend>Enter a command:</legend>
        <ControlledInput
          ref={inputRef}
          value={commandString}
          setValue={setCommandString}
          ariaLabel="Command Input Field"
        />
      </fieldset>
      <button
        aria-label="Submit Command Button"
        onClick={() => (async () => await handleSubmit(commandString))()}
      >
        Submit
      </button>
    </div>
  );
}
