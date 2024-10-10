import {BaseDialogData} from "../base-dialog/base-dialog.data";

export interface CreateGameDialogData extends BaseDialogData {
  gameName: string;
  creatorName: string;
}
