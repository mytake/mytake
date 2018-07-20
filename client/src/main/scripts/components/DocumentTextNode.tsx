import * as React from "react";
import { FoundationNode } from "../utils/functions";
import { alertErr } from "../utils/functions";

interface DataAttributes {
  "data-char-offset": number;
}

interface DocumentTextNodeProps {
  documentNode: FoundationNode;
}

interface DocumentTextNodeState {}

class DocumentTextNode extends React.Component<
  DocumentTextNodeProps,
  DocumentTextNodeState
> {
  constructor(props: DocumentTextNodeProps) {
    super(props);
  }
  render() {
    const { documentNode } = this.props;

    let attributes: DataAttributes = {
      "data-char-offset": documentNode.offset
    };

    switch (documentNode.component) {
      case "h2":
        return <h2 {...attributes}>{documentNode.innerHTML}</h2>;
      case "h3":
        return <h3 {...attributes}>{documentNode.innerHTML}</h3>;
      case "p":
        return <p {...attributes}>{documentNode.innerHTML}</p>;
      default:
        alertErr("DocumentTextNode: Unknown documentNode.component");
        throw "Unknown documentNode.component";
    }
  }
}

export default DocumentTextNode;
