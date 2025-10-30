import { StationState } from './stationState';

export interface Station {
  readonly id: number | null;
  stationName: string;
	latitude: number; // -90 à 90
	longitude: number; // -180 à 180
	priceRate: number; // > 0.1
	powerOutput: number; // > 0.1
	manual?: string;
	state: StationState;
	grounded: boolean;
	busy: boolean;
	wired: boolean;
	spot_id: number;
	owner_id?: number;
	plugTypeList: number[];
}