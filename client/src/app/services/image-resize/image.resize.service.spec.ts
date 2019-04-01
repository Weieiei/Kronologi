import { TestBed } from '@angular/core/testing';

import { ImageResizeService } from './image.resize.service';

describe('ImageResizeService', () => {
      beforeEach(() => TestBed.configureTestingModule({}));

      it('should be created', () => {
            const service: ImageResizeService = TestBed.get(ImageResizeService);
            expect(service).toBeTruthy();
      });
});
