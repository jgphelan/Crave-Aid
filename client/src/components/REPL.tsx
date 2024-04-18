import { useState } from "react";
import "../styles/main.css";
import { REPLHistory } from "./REPLHistory";
import { REPLInput } from "./REPLInput";

interface REPLProps {
  setKeywordSearch: (keyword: string) => void;
}

/** controls the main history and input components of the webpage
 *
 * @returns the repl blocks on the webpage
 */
export default function REPL(props: REPLProps) {
  const [history, setHistory] = useState<any[]>([]);
  const [verbose, setVerbose] = useState<boolean>(false);
  return (
    <div className="repl" aria-label="REPL and Search capability container">
      <REPLHistory history={history} verbose={verbose} />
      <hr></hr>
      <REPLInput
        history={history}
        setHistory={setHistory}
        verbose={verbose}
        setVerbose={setVerbose}
        setKeywordSearch={props.setKeywordSearch}
      />
    </div>
  );
}
