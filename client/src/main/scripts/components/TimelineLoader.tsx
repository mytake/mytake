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
import React, { useState, useEffect } from "react";
import TimelineView from "./TimelineView";
import { SetFactHandlers } from "./TimelinePreview";
import TimelineLoadingView from "./TimelineLoadingView";
import { Foundation } from "../java2ts/Foundation";
import { Routes } from "../java2ts/Routes";
import { validateResponse } from "../utils/foundationData/FoundationDataBuilder";

interface TimelineLoaderProps {
  path: string;
  setFactHandlers?: SetFactHandlers;
}

interface TimelineLoaderState {
  facts?: Foundation.FactLink[];
}

const TimelineLoader: React.FC<TimelineLoaderProps> = (props) => {
  const [state, setState] = useState<TimelineLoaderState>({});

  useEffect(() => {
    async function getAllFacts() {
      const indexHeaders = new Headers();

      indexHeaders.append("Accept", "application/json");
      indexHeaders.append("Cache-Control", "no-cache");

      const indexRequestOptions: RequestInit = {
        method: "GET",
        headers: indexHeaders,
        cache: "no-cache", // https://developer.mozilla.org/en-US/docs/Web/API/Request/cache
      };

      const indexHashRes = await fetch(
        Routes.FOUNDATION_INDEX_HASH,
        indexRequestOptions
      );

      validateResponse(indexHashRes, Routes.FOUNDATION_INDEX_HASH);

      const indexPointer: Foundation.IndexPointer = await indexHashRes.json();

      const headers = new Headers();
      headers.append("Accept", "application/json");
      const requestOptions: RequestInit = {
        method: "GET",
        headers: headers,
        cache: "default",
      };

      const foundationRes = await fetch(
        Routes.FOUNDATION_DATA + "/" + indexPointer.hash + ".json",
        requestOptions
      );

      validateResponse(foundationRes, Routes.FOUNDATION_DATA);

      const allFacts: Foundation.FactLink[] = await foundationRes.json();
      setState({
        facts: allFacts,
      });
    }

    getAllFacts();
  }, []);

  return state.facts ? (
    <TimelineView
      factLinks={state.facts}
      path={props.path}
      setFactHandlers={props.setFactHandlers}
    />
  ) : (
    <TimelineLoadingView />
  );
};

export default TimelineLoader;
