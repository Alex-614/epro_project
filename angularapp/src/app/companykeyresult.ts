import { Businessunit } from "./businessunit"

export interface Companykeyresult {
    id: string,
    title: string,
    type: string,
    goal: number,
    description: string,
    current: number,
    confidenceLevel: number,
    achievement: number //TODO change name with "e",
    contributingBusinessUnits: { id: number, companyId: string}[]
}
//TODO: add owner to keyresult