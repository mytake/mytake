// https://facebook.github.io/jest/docs/snapshot-testing.html#content

import * as React from "react";
import * as renderer from "react-test-renderer";
import BlockReader from "./BlockReader";
import { TakeDocument } from "./BlockEditor";

const doc: TakeDocument = {
  title: "My take title",
  blocks: [
    {
      kind: "paragraph",
      text: ""
    }
  ]
};

jest.mock("./ReactionContainer", () => ({
  default: "ReactionContainer"
}));

function createNodeMock(element: React.ReactElement<HTMLElement>) {
  switch (element.type) {
    case "div": {
      return {
        resetHeight() {
          // Do nothing
        }
      };
    }
    case "textarea": {
      return {
        focus() {
          // Do nothing
        }
      };
    }
    case "p": {
      return {
        resetHeight() {
          // Do nothing
        }
      };
    }
    case "h1": {
      return {
        focus() {
          // Do nothing
        }
      };
    }
  }
}

test("Read only", () => {
  // Object.defineProperty(window.location, 'pathname', {
  // 	writable: true,
  // 	value: 'anything'
  // });

  const tree = renderer
    .create(<BlockReader initState={doc} takeId={2} />, { createNodeMock })
    .toJSON();
  expect(tree).toMatchSnapshot();
});
