/*
 * MyTake.org website and tooling.
 * Copyright (C) 2018-2020 MyTake.org, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact us at team@mytake.org
 */
import React from "react";
import renderer from "react-test-renderer";
import { VideoTurn } from "../../common/social/social";
import { kennedyNixon } from "../../utils/testUtils";
import { HitContentProps } from "./HitContent";
import { HitMock } from "./HitContent.spec";
import { SharePreviewProps } from "./SharePreview";
import VideoResult from "./VideoResult";

jest.mock("./SharePreview", () => ({
  __esModule: true,
  default: (props: SharePreviewProps) => HitMock("SharePreview")(props),
}));

jest.mock("./HitContent", () => ({
  __esModule: true,
  default: (props: HitContentProps) => HitMock("HitContent")(props),
}));

test("VideoResultPreview containing", () => {
  const videoTurn: VideoTurn = {
    kind: "videoTurn",
    fact: "factHash",
    turn: 0,
    cut: [14, 239],
    bold: [[18, 28]],
  };
  const tree = renderer
    .create(
      <VideoResult
        bookmarks={[]}
        videoFact={kennedyNixon}
        videoTurn={videoTurn}
        onPlayClick={jest.fn()}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("VideoResultPreview before and after", () => {
  const videoTurn: VideoTurn = {
    kind: "videoTurn",
    fact: "factHash",
    turn: 0,
    cut: [0, 276],
    bold: [[18, 28]],
  };
  const tree = renderer
    .create(
      <VideoResult
        bookmarks={[]}
        videoFact={kennedyNixon}
        videoTurn={videoTurn}
        onPlayClick={jest.fn()}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});