import {BaseFormData} from "../base-dialog/base-form.data";

export interface CreateGameDialogData extends BaseFormData {
  gameName: string;
  creatorName: string;
}
