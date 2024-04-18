import { useEffect, useState } from "react";
import "../styles/main.css";
/**
 * interface that holds the repl history informstion and whether the output mode is verbose
 */
interface REPLHistoryProps {
  history: any[];
  verbose: boolean;
}
/** Makes an HTML table out of data given as an any[][]
 *
 * @param object a [][] that represnts csv data
 * @returns an html table containing the data of object
 */
function makeHTMLTable(object: any[][]) {
  return (
    <table className="html-table" aria-label="Output Results">
      {object.map((command, index) => (
        <tr key={index}>
          {command.map((command2, index2) => (
            <td key={index2}>{command2}</td>
          ))}
        </tr>
      ))}
    </table>
  );
}

/** creates abd returns a scrollable block of repl cimmand and result history
 *
 * @param props contains fields history, which is an any[] and verbose, a boolean
 * @returns a scrollable box of repl command and result history
 */
export function REPLHistory(props: REPLHistoryProps) {
  // Helper function to determine if the result is an array (indicating success and data)
  const isResultArray = (result: any): result is string[][] => {
    return Array.isArray(result) && Array.isArray(result[0]);
  };

  return (
    <div className="repl-history">
      {props.history.map((entry, index) => {
        const command = entry.command;
        const json = entry.result;
        const result = json.result;
        const data = json.output;
        let content;
        let output;

        if (data) {
          output = data;
        } else {
          output = json;
        }
        console.log(output);
        if (props.verbose) {
          //   Verbose mode output
          if (typeof output === "string") {
            content = <p>{output}</p>;
          } else {
            content = makeHTMLTable(output);
          }
          //   Return verbose mode structure
          return (
            <div key={index} className="history-entry">
              <div className="outputCommand">Command: {command}</div>
              <div className="outputCommand">Output:</div>
              {content}
            </div>
          );
        } else {
          if (typeof output === "string") {
            content = <p>{output}</p>;
          } else {
            content = makeHTMLTable(output);
          }
          return (
            <div key={index} className="history-entry">
              {content}
            </div>
          );
        }
      })}
    </div>
  );
}
