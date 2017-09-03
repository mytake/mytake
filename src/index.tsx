import * as React from "react";
import * as ReactDOM from "react-dom";
import MyTake from "./components/MyTake";
import config from "./components/MyTake/config";
import { FoundationTextType } from "./components/Foundation";
import { validators } from "./utils/functions";

const AMENDMENTS: FoundationTextType = "AMENDMENTS";
const CONSTITUTION: FoundationTextType = "CONSTITUTION";

let app = document.getElementById("app");
let initJson;
if (app && app.hasAttribute("data-init")) {
  let data = app.getAttribute("data-init");
  if (data) {
    initJson = JSON.parse(data);
  } else {
    initJson = config.initialState;
  }
} else if (window.location.hash) {
  // Expect hash URL to be like, #{FoundationType}&{URL of Take being read}&{highlightRangeStart}&{highlightRangeEnd}
  // localhost:3000/new-take/#amendments&/samples/does-a-law-mean-what-it-says-or-what-it-meant/&369&514
  let hashes = window.location.hash.toLowerCase().split("&");
  let foundationType = hashes[0].substring(1).toUpperCase();
  //let article = hashes[1];
  let range = [parseInt(hashes[2]), parseInt(hashes[3])];
  let kind; //document or video
  switch (foundationType) {
    case AMENDMENTS:
      kind = "document";
      break;
    case CONSTITUTION:
      kind = "document";
      break;
    default:
      break;
  }
  if (
    validators.isFoundationTextType(foundationType) &&
    !isNaN(range[0]) &&
    !isNaN(range[1])
  ) {
    initJson = {
      takeDocument: {
        title: "",
        blocks: [
          // Currently only works for Foundation documents, need to add a video case
          { kind: kind, document: foundationType, range: range },
          { kind: "paragraph", text: "" }
        ]
      },
      activeBlockIndex: -1
    };
  } else {
    initJson = config.initialState;
  }
} else {
  initJson = config.initialState;
}

const Root = <MyTake initState={initJson} />;

ReactDOM.render(Root, app);
