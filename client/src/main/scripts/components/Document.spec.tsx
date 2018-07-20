import * as React from "react";
import * as renderer from "react-test-renderer";
import Document from "./Document";
import { documentNodes } from "../utils/testUtils";

const className = "document__row";
const onMouseUp = jest.fn();
const onScroll = jest.fn();

const eventHandlers = {
  onMouseUp: onMouseUp,
  onScroll: onScroll
};

jest.mock("./DocumentTextNodeList", () => ({
  default: "DocumentTextNodeList"
}));

jest.mock("./CaptionTextNodeList", () => ({
  default: "CaptionTextNodeList"
}));

test("Document component", () => {
  const tree = renderer
    .create(
      <Document
        nodes={documentNodes}
        className={className}
        eventHandlers={eventHandlers}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

const children = (
  <div className="editor__block editor__block--overlay" style={{ top: "16px" }}>
    <div className="editor__document editor__document--hover">
      <p data-char-offset="0">
        Section 1.{" "}
        <span className="document__text--selected">
          Neither slavery nor involuntary servitude
        </span>, except as a punishment for crime whereof the party shall have
        been duly convicted, shall exist within the United States, or any place
        subject to their jurisdiction.
      </p>
    </div>
  </div>
);

test("Document component with highlights", () => {
  const tree = renderer
    .create(
      <Document
        nodes={documentNodes}
        className={className}
        eventHandlers={eventHandlers}
      >
        {children}
      </Document>
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});
